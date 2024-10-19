package com.eul4.listener.inventory;

import com.eul4.Main;
import com.eul4.common.event.GuiClickEvent;
import com.eul4.common.event.GuiCloseEvent;
import com.eul4.model.inventory.ArmorySelectOrStorageItemsGui;
import com.eul4.model.player.spiritual.InventoryOrganizerPlayer;
import com.eul4.model.town.structure.Armory;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

@RequiredArgsConstructor
public class ArmorySelectOrStorageItemsGuiListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof InventoryOrganizerPlayer inventoryOrganizerPlayer)
				|| !(inventoryOrganizerPlayer.getGui() instanceof ArmorySelectOrStorageItemsGui))
		{
			return;
		}
		
		event.setCancelled(false);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGuiClose(GuiCloseEvent event)
	{
		if(!(event.getGui() instanceof ArmorySelectOrStorageItemsGui gui)
				|| !(event.getGui().getCommonPlayer() instanceof InventoryOrganizerPlayer inventoryOrganizerPlayer))
		{
			return;
		}
		
		Armory armory = gui.getArmory();
		armory.setStorageContents(gui.getInventory().getContents());
		armory.setBattleInventory(inventoryOrganizerPlayer.getPlayer().getInventory());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGuiClick(GuiClickEvent guiEvent)
	{
		InventoryClickEvent event = guiEvent.getInventoryClickEvent();
		
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof InventoryOrganizerPlayer inventoryOrganizerPlayer)
				|| !(guiEvent.getGui() instanceof ArmorySelectOrStorageItemsGui armorySelectOrStorageItemsGui))
		{
			return;
		}
		
		event.setCancelled(false);
	}
}
