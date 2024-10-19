package com.eul4.listener.inventory;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.inventory.AssistantGui;
import com.eul4.model.inventory.craft.CraftStructureShopGui;
import com.eul4.model.player.physical.TownPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class AssistantGuiListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player))
		{
			return;
		}
		
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(!(commonPlayer.getGui() instanceof AssistantGui))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player))
		{
			return;
		}
		
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(!(commonPlayer.getGui() instanceof AssistantGui assistantGui))
		{
			return;
		}
		
		event.setCancelled(true);
		
		if(!(commonPlayer instanceof TownPlayer townPlayer))
		{
			return;
		}
		
		final ItemStack currentItem = event.getCurrentItem();
		
		if(currentItem == null)
		{
			return;
		}
		
		if(currentItem.equals(assistantGui.getMoveAssistantIcon()))
		{
			player.closeInventory();
			townPlayer.getTown().startMoveAssistant();
		}
		else if(currentItem.equals(assistantGui.getStructureShopIcon()))
		{
			townPlayer.openGui(new CraftStructureShopGui(townPlayer));
		}
		else if(currentItem.equals(assistantGui.getBackToSpawnIcon()))
		{
			player.closeInventory();
			townPlayer.performSpawn();
		}
	}
}
