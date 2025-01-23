package com.eul4.listener.inventory;

import com.eul4.Main;
import com.eul4.common.event.GuiClickEvent;
import com.eul4.model.inventory.TownHallGui;
import com.eul4.model.inventory.craft.CraftAssistantGui;
import com.eul4.model.player.physical.TownPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class TownHallGuiListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryClick(GuiClickEvent guiClickEvent)
	{
		InventoryClickEvent inventoryClickEvent = guiClickEvent.getInventoryClickEvent();
		
		if(!(guiClickEvent.getGui() instanceof TownHallGui townHallGui))
		{
			return;
		}
		
		inventoryClickEvent.setCancelled(true);
		
		if(!(townHallGui.getCommonPlayer() instanceof TownPlayer townPlayer))
		{
			return;
		}
		
		final ItemStack currentItem = inventoryClickEvent.getCurrentItem();
		
		if(currentItem == null)
		{
			return;
		}
		
		if(currentItem.equals(townHallGui.getAssistantMenuIcon()))
		{
			townPlayer.openGui(new CraftAssistantGui(townPlayer));
		}
	}
}
