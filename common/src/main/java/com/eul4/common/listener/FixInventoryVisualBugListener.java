package com.eul4.common.listener;

import com.eul4.common.Common;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

@RequiredArgsConstructor
public class FixInventoryVisualBugListener implements Listener
{
	private final Common plugin;
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockCanBuild(BlockCanBuildEvent event)
	{
		final Player player = event.getPlayer();
		
		if(player != null && !event.isBuildable())
		{
			plugin.getServer().getScheduler().runTask(plugin, player::updateInventory);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(InventoryClickEvent event)
	{
		if(event.getWhoClicked() instanceof Player player && event.isCancelled())
		{
			plugin.getServer().getScheduler().runTask(plugin, player::updateInventory);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		if(event.getWhoClicked() instanceof Player player && event.isCancelled())
		{
			plugin.getServer().getScheduler().runTask(plugin, player::updateInventory);
		}
	}
}
