package com.eul4.model.player;

import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.type.player.SpiritualPlayerType;

public interface SpiritualPlayer extends PluginPlayer
{
	void setReincarnationType(PhysicalPlayerType physicalPlayerType);
	
	@Override
	SpiritualPlayerType getPlayerType();
}
