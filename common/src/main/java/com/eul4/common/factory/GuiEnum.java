package com.eul4.common.factory;

import com.eul4.common.model.inventory.Gui;
import com.eul4.common.model.player.CommonPlayer;

import java.util.function.Function;

public interface GuiEnum
{
	GuiInstantiation getInstantiation();
}
