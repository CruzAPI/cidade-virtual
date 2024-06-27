package com.eul4.model.town.structure;

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
	void setBattleInventory(PlayerInventory playerInventory);
	
	Inventory getStorageInventoryClone();
	
	default HashMap<Integer, ItemStack> addItemToStorage(ItemStack... itemStacks)
	{
		Inventory clone = getStorageInventoryClone();
		HashMap<Integer, ItemStack> remainedItems = clone.addItem(itemStacks);
		setStorageContents(clone.getContents());
		return remainedItems;
	}
}
