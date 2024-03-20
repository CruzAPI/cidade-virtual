package com.eul4.town.model.player;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.town.model.town.Town;

public interface TownPlayer extends CommonPlayer
{
	Town getTown();
	boolean hasTown();
}
