package com.eul4.listener;

import com.destroystokyo.paper.loottable.LootableEntityInventory;
import com.eul4.Main;
import com.eul4.common.util.ContainerUtil;
import com.eul4.common.util.ItemStackUtil;
import com.eul4.common.world.CommonWorld;
import com.eul4.enums.Rarity;
import com.eul4.externalizer.filer.BlockDataFiler;
import com.eul4.service.BlockData;
import com.eul4.util.RarityUtil;
import com.eul4.world.RaidLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.AsyncStructureGenerateEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructurePiece;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import java.util.*;

@RequiredArgsConstructor
public class StructureRarityListener implements Listener
{
	@RequiredArgsConstructor
	private enum StructureRarity
	{
		COMMON(0.1D),
		UNCOMMON(0.2D),
		RARE(0.3D),
		;
		
		private static final Set<Structure> UNCOMMON_STRUCTURES = Set.of
		(
			Structure.FORTRESS,
			Structure.BASTION_REMNANT,
			Structure.VILLAGE_DESERT,
			Structure.VILLAGE_PLAINS,
			Structure.VILLAGE_SAVANNA,
			Structure.VILLAGE_SNOWY,
			Structure.VILLAGE_TAIGA,
			Structure.SWAMP_HUT,
			Structure.DESERT_PYRAMID,
			Structure.MINESHAFT,
			Structure.MINESHAFT_MESA,
			Structure.TRIAL_CHAMBERS
		);
		
		private static final Set<Structure> RARE_STRUCTURES = Set.of
		(
			Structure.END_CITY,
			Structure.MANSION,
			Structure.PILLAGER_OUTPOST,
			Structure.IGLOO,
			Structure.JUNGLE_PYRAMID
		);
		
		private final double exponentiationBase;
		
		public Rarity getRandomRarity(Random random)
		{
			float nextFloat = random.nextFloat();
			
			Rarity[] rarities = Rarity.values();
			
			for(int i = rarities.length - 1; i >= 0; i--)
			{
				Rarity rarity = rarities[i];
				float bound = (float) Math.pow(exponentiationBase, i);
				
				if(nextFloat < bound)
				{
					return rarity;
				}
			}
			
			return Rarity.MIN_RARITY;
		}
	}
	
	private final Main plugin;
	
	private final Random random = new Random();
	
	@EventHandler
	public void setRarityOnLootGeneration(LootGenerateEvent event)
	{
		Rarity rarity;
		
		if(event.getInventoryHolder() instanceof BlockInventoryHolder blockInventoryHolder)
		{
			rarity = RarityUtil.getRarity(plugin, blockInventoryHolder.getBlock());
		}
		else if(event.getInventoryHolder() instanceof LootableEntityInventory lootableEntityInventory)
		{
			rarity = RarityUtil.getRarity(lootableEntityInventory.getEntity());
		}
		else
		{
			return;
		}
		
		for(ItemStack item : event.getLoot())
		{
			RarityUtil.setRarity(item, rarity);
			
			if(ItemStackUtil.hasEnchantments(item))
			{
				ItemStackUtil.clearEnchantments(item);
				ItemStack enchanted = EnchantmentListener.enchantItemRandomLevel(item, random);
				item.setItemMeta(enchanted.getItemMeta());
			}
		}
	}
	
	@EventHandler
	public void setStructureRarityOnGenerate(AsyncStructureGenerateEvent event)
	{
		World world = event.getWorld();
		BlockDataFiler blockDataFiler = plugin.getBlockDataFiler();
		
		CommonWorld commonWorld = plugin.getWorldManager().get(world);
		
		if(!(commonWorld instanceof RaidLevel))
		{
			return;
		}
		
		Rarity rarity = RarityUtil.getOrSetRarity(event.getGeneratedStructure(),
				() -> getStructureRarity(event.getStructure()).getRandomRarity(random));
		
		UUID structureUniqueId = ContainerUtil.getOrGenerateRandomUUID(event.getGeneratedStructure());
		
		plugin.getServer().getScheduler().runTask(plugin, () -> world.getChunkAtAsync(event.getChunkX(), event.getChunkZ())
			.whenComplete((chunk, throwable) ->
			{
				if(chunk == null)
				{
					return;
				}
				
				BoundingBox chunkBox = BoundingBox.of
				(
					chunk.getBlock(0, chunk.getWorld().getMinHeight(), 0),
					chunk.getBlock(15, chunk.getWorld().getMaxHeight(), 15)
				);
				
				for(GeneratedStructure generatedStructure : chunk.getStructures(event.getStructure()))
				{
					if(!structureUniqueId.equals(ContainerUtil.getUUID(generatedStructure)))
					{
						continue;
					}
					
					List<BoundingBox> pieces = getPieces(generatedStructure).stream()
							.filter(bb -> bb.overlaps(chunkBox))
							.map(bb -> bb.intersection(chunkBox))
							.toList();
					
					for(Entity entity : chunk.getEntities())
					{
						for(BoundingBox piece : pieces)
						{
							if(piece.contains(entity.getLocation().toVector()))
							{
								RarityUtil.setRarity(entity, rarity);
							}
						}
					}
					
					for(BoundingBox piece : pieces)
					{
						for(int x = piece.getMin().getBlockX(); x <= piece.getMax().getBlockX(); x++)
						{
							for(int y = piece.getMin().getBlockY(); y <= piece.getMax().getBlockY(); y++)
							{
								for(int z = piece.getMin().getBlockZ(); z <= piece.getMax().getBlockZ(); z++)
								{
									final Block block = world.getBlockAt(x, y, z);
									
									if(!block.isEmpty())
									{
										blockDataFiler.loadBlockDataOrDefault(block, () -> new BlockData(rarity));
									}
								}
							}
						}
					}
				}
			}));
	}
	
	private List<BoundingBox> getPieces(GeneratedStructure generatedStructure)
	{
		List<BoundingBox> boundingBoxList = new ArrayList<>();
		
		for(StructurePiece piece : generatedStructure.getPieces())
		{
			boundingBoxList.add(piece.getBoundingBox());
		}
		
		boundingBoxList.addAll(getMissingPieces(generatedStructure));
		
		return boundingBoxList;
	}
	
	private List<BoundingBox> getMissingPieces(GeneratedStructure generatedStructure)
	{
		List<BoundingBox> missingPieces = new ArrayList<>();
		
		if(generatedStructure.getStructure() == Structure.DESERT_PYRAMID)
		{
			Iterator<StructurePiece> iterator = generatedStructure.getPieces().iterator();
			
			if(!iterator.hasNext())
			{
				return missingPieces;
			}
			
			StructurePiece piece = iterator.next();
			BoundingBox bb = piece.getBoundingBox();
			missingPieces.add(new BoundingBox(bb.getMinX(), bb.getMinY() - 14.0D, bb.getMinZ(),
					bb.getMaxX(), bb.getMinY(), bb.getMaxZ()));
		}
		else if(generatedStructure.getStructure() == Structure.JUNGLE_PYRAMID)
		{
			Iterator<StructurePiece> iterator = generatedStructure.getPieces().iterator();
			
			if(!iterator.hasNext())
			{
				return missingPieces;
			}
			
			StructurePiece piece = iterator.next();
			BoundingBox bb = piece.getBoundingBox();
			missingPieces.add(new BoundingBox(bb.getMinX(), bb.getMinY() - 4.0D, bb.getMinZ(),
					bb.getMaxX(), bb.getMinY(), bb.getMaxZ()));
		}
		
		return missingPieces;
	}
	
	private StructureRarity getStructureRarity(Structure structure)
	{
		if(StructureRarity.RARE_STRUCTURES.contains(structure))
		{
			return StructureRarity.RARE;
		}
		
		if(StructureRarity.UNCOMMON_STRUCTURES.contains(structure))
		{
			return StructureRarity.UNCOMMON;
		}
		
		return StructureRarity.COMMON;
	}
}
