package com.eul4.model.town.structure;

import org.bukkit.inventory.ItemStack;

public interface Armory extends Structure
{
	ItemStack[] getInventoryContents();
	void setInventoryContents(ItemStack[] contents);
	ItemStack[] getBattleInventoryContents();
	void setBattleInventoryContents(ItemStack[] contents);
}
