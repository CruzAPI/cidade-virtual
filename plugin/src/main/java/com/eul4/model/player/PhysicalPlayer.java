package com.eul4.model.player;

import com.eul4.type.player.PhysicalPlayerType;

public interface PhysicalPlayer extends PluginPlayer
{
	@Override
	PhysicalPlayerType getPlayerType();
}
