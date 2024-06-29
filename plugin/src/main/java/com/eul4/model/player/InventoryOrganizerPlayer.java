package com.eul4.model.player;

import org.bukkit.inventory.ItemStack;

public interface InventoryOrganizerPlayer extends PluginPlayer, SpiritualPlayer, Spectator
{
	ItemStack[] getContents();
	void onCloseInventory();
	void onSneak();
}
