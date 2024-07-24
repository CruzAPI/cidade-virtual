package com.eul4.model.player;

import com.eul4.scoreboard.TownScoreboard;

public interface SpawnPlayer extends
		PhysicalPlayer,
		TownPerformer.Reincarnate,
		SpawnPerformer.Teleport,
		PluginScoreboardPlayer,
		Invincible
{
	@Override
	TownScoreboard getScoreboard();
	
	PluginPlayer removeProtection();
}
