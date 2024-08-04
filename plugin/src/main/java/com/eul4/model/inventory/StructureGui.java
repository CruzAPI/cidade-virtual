package com.eul4.model.inventory;

import com.eul4.common.model.inventory.Gui;
import com.eul4.model.town.structure.Structure;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

public interface StructureGui extends Gui
{
	Structure getStructure();
	Component getUpdatedTitleComponent();
	
	ItemStack getUpgradeIcon();
}
