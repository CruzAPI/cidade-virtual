package com.eul4.model.inventory;

import com.eul4.common.model.inventory.Gui;
import com.eul4.model.inventory.craft.CraftArmoryWeaponShopGui;

public interface ArmoryWeaponShopGui extends Gui
{
	CraftArmoryWeaponShopGui.Icon getBySlot(int slot);
	
	boolean isLocked(CraftArmoryWeaponShopGui.Icon icon);
}
