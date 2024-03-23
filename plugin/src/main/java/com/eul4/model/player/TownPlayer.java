package com.eul4.model.player;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;

import java.util.Optional;

public interface TownPlayer extends CommonPlayer
{
	Town getTown();
	boolean hasTown();
	Structure getMovingStructure();
	void setMovingStructure(Structure structure);
}
