package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.util.ContainerUtil;
import com.eul4.common.util.ItemStackUtil;
import com.eul4.enums.Rarity;
import com.eul4.externalizer.filer.BlockDataFiler;
import com.eul4.service.BlockData;
import com.eul4.util.RarityUtil;
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
	private final Main plugin;
	
	private final Random random = new Random();
	
	@EventHandler
	public void setRarityOnLootGeneration(LootGenerateEvent event)
	{
		if(!(event.getInventoryHolder() instanceof BlockInventoryHolder blockInventoryHolder))
		{
			return;
		}
		
		Block block = blockInventoryHolder.getBlock();
		Rarity rarity = RarityUtil.getRarity(plugin, block);
		
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
		
		Rarity rarity = RarityUtil.findRarity(event.getGeneratedStructure())
				.orElseGet(() ->
				{
					Rarity randomRarity = Rarity.values()[random.nextInt(Rarity.values().length)];
					RarityUtil.setRarity(event.getGeneratedStructure(), randomRarity);
					return randomRarity;
				});
		
		
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
									blockDataFiler.loadBlockDataOrDefault(world.getBlockAt(x, y, z), () -> new BlockData(rarity));
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
}
