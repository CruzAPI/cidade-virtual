package com.eul4.common.listener;

import com.eul4.common.Common;
import com.eul4.common.constant.CommonNamespacedKey;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

import static org.bukkit.event.inventory.InventoryAction.DROP_ALL_CURSOR;
import static org.bukkit.event.inventory.InventoryAction.DROP_ONE_CURSOR;

@RequiredArgsConstructor
public class CancelItemDropListener implements Listener
{
	private final Common plugin;
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		ItemStack itemStack = event.getItemDrop().getItemStack();
		Player player = event.getPlayer();
		PlayerInventory inventory = player.getInventory();
		
		if(!isDropCancelled(itemStack))
		{
			return;
		}
		
		event.setCancelled(true);
		
		if(inventory.firstEmpty() == -1 && !isAvailable(inventory, itemStack))
		{
			Optional.ofNullable(inventory.addItem(itemStack).get(0))
					.ifPresent(player::setItemOnCursor);
		}
	}
	
	private boolean isAvailable(Inventory inventory, ItemStack itemStack)
	{
		int available = 0;
		
		for(final ItemStack currentItem : inventory.getStorageContents())
		{
			if(currentItem == null)
			{
				return true;
			}
			
			if(currentItem.isSimilar(itemStack))
			{
				available += currentItem.getMaxStackSize() - currentItem.getAmount();
			}
			
			if(available >= itemStack.getAmount())
			{
				return true;
			}
		}
		
		return false;
	}
	
	@EventHandler
	public void onPlayerDropItem(InventoryClickEvent event)
	{
		final ItemStack cursor = event.getCursor();
		final InventoryAction action = event.getAction();
		final boolean cursorDrop = action == DROP_ONE_CURSOR || action == DROP_ALL_CURSOR;
		
		if(isDropCancelled(cursor) && cursorDrop)
		{
			event.setCancelled(true);
		}
	}
	
	private boolean isDropCancelled(ItemStack item)
	{
		if(item == null || !item.hasItemMeta())
		{
			return false;
		}
		
		final var container = item.getItemMeta().getPersistentDataContainer();
		
		if(!container.has(CommonNamespacedKey.CANCEL_DROP, PersistentDataType.BOOLEAN))
		{
			return false;
		}
		
		return container.get(CommonNamespacedKey.CANCEL_DROP, PersistentDataType.BOOLEAN);
	}
}
