package com.eul4.common.listener;

import com.eul4.common.Common;
import com.eul4.common.model.player.CommonAdmin;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public class CommonAdminListener implements Listener
{
	private final Common plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPickupItem(EntityPickupItemEvent event)
	{
		if(!(event.getEntity() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
}
