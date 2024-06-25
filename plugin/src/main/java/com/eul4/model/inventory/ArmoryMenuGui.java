package com.eul4.model.inventory;

import com.eul4.common.model.inventory.Gui;
import com.eul4.model.town.structure.Armory;
import org.bukkit.inventory.ItemStack;

public interface ArmoryMenuGui extends Gui
{
	ItemStack getShopIcon();
	ItemStack getMyInventoryIcon();
	Armory getArmory();
}
