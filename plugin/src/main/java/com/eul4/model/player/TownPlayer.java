package com.eul4.model.player;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.town.Town;

public interface TownPlayer extends CommonPlayer
{
	Town getTown();
}
