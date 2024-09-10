package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.i18n.Messageable;
import com.eul4.enums.Rarity;
import com.eul4.i18n.PluginMessage;
import com.eul4.util.RarityUtil;
import com.eul4.util.SoundUtil;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class ContainerRarityListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		final ItemStack oldCursor = event.getOldCursor();
		
		if(hasAnySlotInUpperInventory(event))
		{
			cancelIfNeededAndSendMessage(oldCursor, event);
		}
	}
	
	private boolean hasAnySlotInUpperInventory(final InventoryDragEvent event)
	{
		if(event.getInventory().getType() == InventoryType.CRAFTING)
		{
			return false;
		}
		
		return getMinRawSlot(event.getRawSlots()) < event.getInventory().getSize();
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
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryShiftClick(InventoryClickEvent event)
	{
		ItemStack currentItem = event.getCurrentItem();
		
		if(event.getClick() != ClickType.SHIFT_LEFT
				&& event.getClick() != ClickType.SHIFT_RIGHT)
		{
			return;
		}
		
		final boolean selfInventory = event.getInventory().getType() == InventoryType.CRAFTING;
		final Inventory clickedInventory = event.getClickedInventory();
		
		if(selfInventory || clickedInventory == null || clickedInventory.getType() != InventoryType.PLAYER)
		{
			return;
		}
		
		cancelIfNeededAndSendMessage(currentItem, event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryHotbarClick(InventoryClickEvent event)
	{
		if(event.getClick() != ClickType.NUMBER_KEY
				|| !(event.getWhoClicked() instanceof Player player))
		{
			return;
		}
		
		final int hotbarButton = event.getHotbarButton();
		final ItemStack hotbarItem = player.getInventory().getItem(hotbarButton);
		
		if(isClickingInUpperContainerSlots(event))
		{
			cancelIfNeededAndSendMessage(hotbarItem, event);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryCursorClick(InventoryClickEvent event)
	{
		ItemStack cursor = event.getCursor();
		
		if(isClickingInUpperContainerSlots(event))
		{
			cancelIfNeededAndSendMessage(cursor, event);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryMoveItem(InventoryMoveItemEvent event)
	{
		cancelIfNeededAndSendMessage(null, event.getItem(), event.getDestination(), event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryPickupItem(InventoryPickupItemEvent event)
	{
		cancelIfNeededAndSendMessage(null, event.getItem().getItemStack(), event.getInventory(), event);
	}
	
	private boolean isClickingInUpperContainerSlots(InventoryClickEvent event)
	{
		final Inventory clickedInventory = event.getClickedInventory();
		final Inventory topInventory = event.getView().getTopInventory();
		
		return clickedInventory == topInventory;
	}
	
	private <E extends InventoryInteractEvent & Cancellable> boolean cancelIfNeededAndSendMessage
	(
		ItemStack itemStack,
		E event
	)
	{
		return cancelIfNeededAndSendMessage(event.getWhoClicked(), itemStack, event.getInventory(), event);
	}
	
	private boolean cancelIfNeededAndSendMessage
	(
		@Nullable HumanEntity humanEntity,
		ItemStack itemStack,
		Inventory inventory,
		Cancellable cancellable
	)
	{
		if(itemStack == null || itemStack.isEmpty())
		{
			return false;
		}
		
		InventoryType inventoryType = inventory.getType();
		Messageable messageable = plugin.getMessageableService().getMessageable(humanEntity);
		
		if(inventoryType == InventoryType.ENDER_CHEST)
		{
			messageable.sendMessage(PluginMessage.ENDERCHEST_DISABLED_YOU_CAN_ONLY_PICKUP);
			SoundUtil.playPlongIfPlayer(humanEntity);
			cancellable.setCancelled(true);
			return true;
		}
		
		Rarity inventoryRarity = getRarity(inventory.getHolder());
		String translationKey = getTranslationKey(inventory.getHolder());
		
		if(inventoryRarity == null || translationKey == null)
		{
			return false;
		}
		
		Rarity itemRarity = RarityUtil.getRarity(itemStack);
		
		switch(inventoryType)
		{
		case CRAFTER:
		case ENCHANTING:
			if(itemRarity != inventoryRarity)
			{
				messageable.sendMessage(PluginMessage.INCOMPATIBLE_RARITY);
				SoundUtil.playPlongIfPlayer(humanEntity);
				cancellable.setCancelled(true);
				return true;
			}
		case ANVIL:
		case CHEST:
		case FURNACE:
		case WORKBENCH:
		case HOPPER:
		case SMITHING:
		default:
			if(itemRarity.compareTo(inventoryRarity) > 0)
			{
				messageable.sendMessage
				(
					itemRarity
							.getContainerIncompatibilityMessage()
							.withArgs(Component.translatable(translationKey))
				);
				
				SoundUtil.playPlongIfPlayer(humanEntity);
				cancellable.setCancelled(true);
				return true;
			}
		}
		
		return false;
	}
	
	private Rarity getDoubleChestRarity(DoubleChest doubleChest)
	{
		Rarity leftRarity = getRarity(doubleChest.getLeftSide());
		Rarity rightRarity = getRarity(doubleChest.getRightSide());
		
		return Rarity.getMinRarity(leftRarity, rightRarity);
	}
	
	private Rarity getRarity(InventoryHolder holder)
	{
		if(holder instanceof DoubleChest doubleChest)
		{
			return getDoubleChestRarity(doubleChest);
		}
		
		if(holder instanceof BlockInventoryHolder blockInventoryHolder)
		{
			Block block = blockInventoryHolder.getBlock();
			return RarityUtil.getRarity(plugin, block);
		}
		
		Inventory inventory = holder.getInventory();
		
		if(inventory.getType() == InventoryType.WORKBENCH)
		{
			Block block = inventory.getLocation().getBlock();
			return RarityUtil.getRarity(plugin, block);
		}
		
		if(inventory.getHolder() instanceof Entity entity && !(entity instanceof Player))
		{
			return RarityUtil.getRarity(entity);
		}
		
		return null;
	}
	
	private String getTranslationKey(InventoryHolder holder)
	{
		if(holder instanceof DoubleChest)
		{
			return Material.CHEST.translationKey();
		}
		
		if(holder instanceof BlockInventoryHolder blockInventoryHolder)
		{
			return blockInventoryHolder.getBlock().translationKey();
		}
		
		Inventory inventory = holder.getInventory();
		
		if(inventory.getType() == InventoryType.WORKBENCH)
		{
			return inventory.getLocation().getBlock().translationKey();
		}
		
		if(inventory.getHolder() instanceof Entity entity)
		{
			return entity.getType().translationKey();
		}
		
		return null;
	}
}
