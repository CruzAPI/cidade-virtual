package com.eul4.common.factory;

import com.eul4.common.model.inventory.Gui;
import com.eul4.common.model.player.CommonPlayer;

@FunctionalInterface
public interface GuiInstantiation
{
	Gui newInstance(CommonPlayer commonPlayer);
}
