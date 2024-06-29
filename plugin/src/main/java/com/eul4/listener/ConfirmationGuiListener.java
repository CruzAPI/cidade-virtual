package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.inventory.ConfirmationGui;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ConfirmationGuiListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player).getGui() instanceof ConfirmationGui))
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
		
		if(!(commonPlayer.getGui() instanceof ConfirmationGui gui))
		{
			return;
		}
		
		event.setCancelled(true);
		
		final ItemStack currentItem = event.getCurrentItem();
		
		if(currentItem == null || event.getClick() != ClickType.LEFT)
		{
			return;
		}
		
		if(currentItem.equals(gui.getConfirm()))
		{
			gui.confirm();
			player.closeInventory();
		}
		else if(currentItem.equals(gui.getCancel()))
		{
			player.closeInventory();
			gui.cancel();
		}
	}
}
