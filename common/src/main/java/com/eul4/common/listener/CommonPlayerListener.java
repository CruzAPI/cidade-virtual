package com.eul4.common.listener;

import com.eul4.common.Common;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

@RequiredArgsConstructor
public class CommonPlayerListener implements Listener
{
	private final Common plugin;
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void updateInventory(InventoryClickEvent event)
	{
		if(event.getWhoClicked() instanceof Player player && event.isCancelled())
		{
			plugin.getServer().getScheduler().runTask(plugin, player::updateInventory);
		}
	}
}
