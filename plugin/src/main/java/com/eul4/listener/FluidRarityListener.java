package com.eul4.listener;

import com.eul4.Main;
import com.eul4.enums.Rarity;
import com.eul4.externalizer.filer.BlockDataFiler;
import com.eul4.util.RarityUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.FluidLevelChangeEvent;

public class FluidRarityListener implements Listener
{
	private final Main plugin;
	
	private final BlockDataFiler blockDataFiler;
	
	public FluidRarityListener(Main plugin)
	{
		this.plugin = plugin;
		
		this.blockDataFiler = plugin.getBlockDataFiler();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onFluidLevelChange(FluidLevelChangeEvent event)
	{
		Block block = event.getBlock();
		BlockData newData = event.getNewData();
		
		if(newData.getMaterial().isEmpty())
		{
			plugin.getBlockDataFiler().removeBlockData(block);
			return;
		}
		
		if(newData instanceof Levelled levelled && levelled.getMaximumLevel() == levelled.getMaximumLevel())
		{
			com.eul4.service.BlockData blockData = plugin.getBlockDataFiler().loadBlockDataOrDefault(block);
			blockData.setRarity(Rarity.COMMON);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockFromTo(BlockFromToEvent event)
	{
		Block block = event.getBlock();
		Block toBlock = event.getToBlock();
		
		Rarity blockRarity = RarityUtil.getRarity(plugin, block);
		
		if(block.getType() == Material.DRAGON_EGG)
		{
			blockDataFiler.removeBlockData(block);
		}
		
		com.eul4.service.BlockData blockData = blockDataFiler.loadBlockDataOrDefault(toBlock);
		blockData.setRarity(blockRarity);
	}
}
