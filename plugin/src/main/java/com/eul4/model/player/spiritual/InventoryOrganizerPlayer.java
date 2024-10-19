package com.eul4.model.player.spiritual;

import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.Spectator;
import com.eul4.model.player.SpiritualPlayer;
import org.bukkit.inventory.ItemStack;

public interface InventoryOrganizerPlayer extends PluginPlayer, SpiritualPlayer, Spectator
{
	ItemStack[] getContents();
	void onCloseInventory();
	void onSneak();
}
