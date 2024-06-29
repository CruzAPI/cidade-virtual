package com.eul4.model.town.structure;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public interface Armory extends Structure
{
	ItemStack[] getStorageContents();
	void setStorageContents(ItemStack[] contents);
	ItemStack[] getBattleInventoryContents();
	void setBattleInventoryContents(ItemStack[] contents);
	
	Inventory getStorageInventoryClone(Component title);
	Inventory getStorageInventoryClone();
	
	void setBattleInventory(PlayerInventory playerInventory);
	
	boolean isEmptyExcludingExtraSlots();
	
	boolean isEmpty();
	boolean isBattleInventoryEmpty();
	boolean isStorageInventoryEmpty();
	
	default HashMap<Integer, ItemStack> addItemToStorage(ItemStack... itemStacks)
	{
		Inventory clone = getStorageInventoryClone();
		HashMap<Integer, ItemStack> remainedItems = clone.addItem(itemStacks);
		setStorageContents(clone.getContents());
		return remainedItems;
	}
}
