package com.eul4.listener;

import com.eul4.Main;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.player.TownPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

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
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
				|| !(townPlayer.getGui() instanceof StructureGui structureGui))
		{
			return;
		}
		
		event.setCancelled(true);
		
		final ItemStack currentItem = event.getCurrentItem();
		
		if(currentItem == null)
		{
			return;
		}
		
		if(event.getSlot() == 1)
		{
			player.closeInventory();
			
			try
			{
				var map = player.getInventory().addItem(structureGui.getStructure().getItem());
				
				structureGui.getStructure().startMove();
				townPlayer.setMovingStructure(structureGui.getStructure());
			}
			catch(CannotConstructException e)
			{
				player.sendMessage("failed to cancel move");
			}
			catch(IOException e)
			{
				player.sendMessage("schem not found?.");
			}
		}
	}
}
