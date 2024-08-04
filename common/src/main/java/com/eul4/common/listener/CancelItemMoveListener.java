package com.eul4.common.listener;

import com.eul4.common.Common;
import com.eul4.common.constant.CommonNamespacedKey;
import com.eul4.common.util.ContainerUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

@RequiredArgsConstructor
public class CancelItemMoveListener implements Listener
{
	private static final int OFF_HAND_SLOT = 40;
	private static final int RAW_OFF_HAND_SLOT = 45;
	
	private final Common plugin;
	
	@EventHandler(ignoreCancelled = true)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		final ItemStack oldCursor = event.getOldCursor();
		
		if(isMoveCancelled(oldCursor) && hasAnySlotInUpperInventory(event))
		{
			event.setCancelled(true);
		}
	}
	
	private boolean hasAnySlotInUpperInventory(final InventoryDragEvent event)
	{
		if(event.getInventory().getType() == InventoryType.CRAFTING
				&& event.getRawSlots().contains(RAW_OFF_HAND_SLOT))
		{
			return true;
		}
		
		// Type CRAFTING size is 5, adding the 4 armor slots totalizing 9.
		int size = event.getInventory().getType() == InventoryType.CRAFTING ? 9 : event.getInventory().getSize();
		
		return getMinRawSlot(event.getRawSlots()) < size;
	}
	
	private int getMinRawSlot(Set<Integer> rawSlots)
	{
		int min = Integer.MAX_VALUE;
		
		for(int rawSlot : rawSlots)
		{
			min = Math.min(rawSlot, min);
		}
		
		return min;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onInventoryShiftClick(InventoryClickEvent event)
	{
		ItemStack currentItem = event.getCurrentItem();
		
		if(!(event.getWhoClicked() instanceof Player player)
				|| event.getClick() != ClickType.SHIFT_LEFT && event.getClick() != ClickType.SHIFT_RIGHT
				|| !isMoveCancelled(currentItem))
		{
			return;
		}
		
		final boolean selfInventory = event.getInventory().getType() == InventoryType.CRAFTING;
		final Inventory clickedInventory = event.getClickedInventory();
		
		if(clickedInventory == null || clickedInventory.getType() != InventoryType.PLAYER)
		{
			return;
		}
		
		if(!selfInventory)
		{
			event.setCancelled(true);
			return;
		}
		
		final EquipmentSlot equipmentSlot = currentItem.getType().getEquipmentSlot();
		
		if(equipmentSlot != EquipmentSlot.HAND && player.getInventory().getItem(equipmentSlot).isEmpty())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onInventoryHotbarClick(InventoryClickEvent event)
	{
		if(event.getClick() != ClickType.NUMBER_KEY
				|| !(event.getWhoClicked() instanceof Player player))
		{
			return;
		}
		
		final int hotbarButton = event.getHotbarButton();
		final ItemStack hotbarItem = player.getInventory().getItem(hotbarButton);
		
		if(!isMoveCancelled(hotbarItem))
		{
			return;
		}
		
		if(isClickingOutside(event))
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onInventoryCursorClick(InventoryClickEvent event)
	{
		ItemStack cursor = event.getCursor();
		
		if(!isMoveCancelled(cursor))
		{
			return;
		}
		
		if(isClickingOutside(event))
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void cancelRightClickEquipArmor(PlayerInteractEvent event)
	{
		ItemStack item = event.getItem();
		
		if(!isMoveCancelled(item))
		{
			return;
		}
		
		if(item.getType().getEquipmentSlot().isArmor())
		{
			event.setUseItemInHand(Event.Result.DENY);
		}
	}
	
	private boolean isClickingOutside(InventoryClickEvent event)
	{
		final Inventory clickedInventory = event.getClickedInventory();
		
		return clickedInventory != null
				&& (clickedInventory.getType() != InventoryType.PLAYER
				|| event.getSlotType() == InventoryType.SlotType.ARMOR
				|| event.getSlot() == OFF_HAND_SLOT);
	}
	
	private boolean isMoveCancelled(ItemStack item)
	{
		return ContainerUtil.hasFlag(item, CommonNamespacedKey.CANCEL_MOVE);
	}
}
