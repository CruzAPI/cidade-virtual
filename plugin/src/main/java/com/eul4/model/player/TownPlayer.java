package com.eul4.model.player;

import com.eul4.common.model.player.ScoreboardPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import com.eul4.scoreboard.TownScoreboard;

public interface TownPlayer extends
		PluginPlayer,
		PhysicalPlayer,
		Invincible,
		TownPerformer.TeleportInside,
		SpawnPerformer.Reincarnation,
		BuyStructurePerformer,
		ScoreboardPlayer
{
	Town getTown();
	boolean hasTown();
	Structure getMovingStructure();
	void setMovingStructure(Structure structure);
	
	void test(boolean test);
	boolean test();
	
	@Override
	TownScoreboard getScoreboard();
}
