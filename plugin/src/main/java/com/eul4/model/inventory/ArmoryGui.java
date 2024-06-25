package com.eul4.model.inventory;

import com.eul4.model.town.structure.Armory;
import org.bukkit.inventory.ItemStack;

public interface ArmoryGui extends StructureGui
{
	ItemStack getMenuIcon();
	
	Armory getArmory();
}
