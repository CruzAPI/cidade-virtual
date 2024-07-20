package com.eul4.model.player;

import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;

public interface TownPlayer extends
		PluginPlayer,
		PhysicalPlayer,
		Invincible,
		TownPerformer.TeleportInside,
		SpawnPerformer.Reincarnation,
		BuyStructurePerformer
{
	Town getTown();
	boolean hasTown();
	Structure getMovingStructure();
	void setMovingStructure(Structure structure);
	
	void test(boolean test);
	boolean test();
}
