package com.eul4.common.model.inventory;

import org.bukkit.entity.Player;

public interface ExtraInventory extends Gui
{
	Player getTarget();
	void updateInventory();
	void applyToPlayer();
}
