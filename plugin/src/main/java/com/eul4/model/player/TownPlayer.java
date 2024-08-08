package com.eul4.model.player;

import com.eul4.model.town.Town;
import com.eul4.scoreboard.TownScoreboard;

public interface TownPlayer extends
		PluginPlayer,
		PhysicalPlayer,
		Invincible,
		TownPerformer.TeleportInside, SpawnPerformer.Reincarnate,
		BuyStructurePerformer,
		TownScoreboardPlayer,
		DelHomePerformer,
		HomePerformer.InstantTeleport
{
	Town getTown();
	boolean hasTown();
	
	void test(boolean test);
	boolean test();
	
	@Override
	TownScoreboard getScoreboard();
}
