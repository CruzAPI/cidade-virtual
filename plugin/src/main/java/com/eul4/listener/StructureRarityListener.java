package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.util.ItemStackUtil;
import com.eul4.enums.Rarity;
import com.eul4.externalizer.filer.BlockDataFiler;
import com.eul4.service.BlockData;
import com.eul4.util.RarityUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.AsyncStructureGenerateEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import java.util.List;
import java.util.Random;

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
		List<BoundingBox> pieces = event.getPieces();
		
		World world = event.getWorld();
		BlockDataFiler blockDataFiler = plugin.getBlockDataFiler();
		
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () ->
		{
			Chunk chunk = world.getChunkAt(event.getChunkX(), event.getChunkZ());
			GeneratedStructure generatedStructure = event.getGeneratedStructure();
			
			Rarity rarity = RarityUtil.findRarity(generatedStructure)
					.orElseGet(() ->
					{
						Rarity randomRarity = Rarity.values()[random.nextInt(Rarity.values().length)];
						RarityUtil.setRarity(generatedStructure, randomRarity);
						return randomRarity;
					});
			
			synchronized(blockDataFiler)
			{
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
		});
	}
}
