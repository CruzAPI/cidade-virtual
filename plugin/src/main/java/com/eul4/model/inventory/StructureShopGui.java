package com.eul4.model.inventory;

import com.eul4.common.model.inventory.Gui;
import com.eul4.enums.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public interface StructureShopGui extends Gui
{
	boolean hasItemBuilder(ItemStack item);
	ItemBuilder getItemBuilder(ItemStack item);
}
