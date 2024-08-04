package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.event.GuiClickEvent;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;

@RequiredArgsConstructor
public class StructureGuiListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player).getGui() instanceof StructureGui structureGui))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onGuiClick(GuiClickEvent event)
	{
		if(!(event.getGui() instanceof StructureGui structureGui)
				|| !(structureGui.getCommonPlayer() instanceof TownPlayer townPlayer))
		{
			return;
		}
		
		Player player = townPlayer.getPlayer();
		
		InventoryClickEvent inventoryClickEvent = event.getInventoryClickEvent();
		inventoryClickEvent.setCancelled(true);
		
		final ItemStack currentItem = inventoryClickEvent.getCurrentItem();
		
		if(currentItem == null || inventoryClickEvent.getClick() != ClickType.LEFT)
		{
			return;
		}
		
		if(currentItem.equals(structureGui.getUpgradeIcon()))
		{
			plugin.getStructureUpgradeExecutor().executeUpgradePursache(townPlayer, structureGui.getStructure());
			player.closeInventory();
		}
		else if(inventoryClickEvent.getSlot() == 1)
		{
			player.closeInventory();
			
			try
			{
				Structure structure = structureGui.getStructure();
				
				structure.removeAllStructureItemMove(player);
				HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(structure.getItem(townPlayer));
				
				if(!remainingItems.isEmpty())
				{
					townPlayer.sendMessage(PluginMessage.STRUCTURE_MOVE_INVENTORY_FULL);
					return;
				}
				
				structure.startMove();
			}
			catch(IOException e)
			{
				townPlayer.sendMessage(PluginMessage.STRUCTURE_SCHEMATIC_NOT_FOUND);
			}
		}
	}
}
