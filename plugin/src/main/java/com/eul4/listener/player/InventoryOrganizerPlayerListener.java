package com.eul4.listener.player;

import com.eul4.Main;
import com.eul4.common.event.GuiClickEvent;
import com.eul4.common.event.GuiCloseEvent;
import com.eul4.model.player.InventoryOrganizerPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

@RequiredArgsConstructor
public class InventoryOrganizerPlayerListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void on(PlayerToggleSneakEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof InventoryOrganizerPlayer inventoryOrganizerPlayer))
		{
			return;
		}
		
		if(event.isSneaking())
		{
			inventoryOrganizerPlayer.onSneak();
			inventoryOrganizerPlayer.reincarnate();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(GuiCloseEvent guiEvent)
	{
		InventoryCloseEvent event = guiEvent.getInventoryCloseEvent();
		
		if(!(event.getPlayer() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof InventoryOrganizerPlayer inventoryOrganizerPlayer))
		{
			return;
		}
		
		inventoryOrganizerPlayer.onCloseInventory();
		inventoryOrganizerPlayer.reincarnate();
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void on(GuiClickEvent guiEvent)
	{
		InventoryClickEvent event = guiEvent.getInventoryClickEvent();
		
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof InventoryOrganizerPlayer))
		{
			return;
		}
		
		event.setCancelled(false);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void on(InventoryDragEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof InventoryOrganizerPlayer))
		{
			return;
		}
		
		event.setCancelled(false);
	}
}
