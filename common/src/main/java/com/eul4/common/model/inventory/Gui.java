package com.eul4.common.model.inventory;

import com.eul4.common.Common;
import com.eul4.common.model.player.CommonPlayer;
import org.bukkit.inventory.Inventory;

public interface Gui
{
	CommonPlayer getCommonPlayer();
	Inventory getInventory();
	Common getPlugin();
	void updateTitle();
}
