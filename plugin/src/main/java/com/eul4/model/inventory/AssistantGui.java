package com.eul4.model.inventory;

import com.eul4.common.model.inventory.Gui;
import org.bukkit.inventory.ItemStack;

public interface AssistantGui extends Gui
{
	ItemStack getMoveAssistantIcon();
	ItemStack getStructureShopIcon();
	ItemStack getBackToSpawnIcon();
}
