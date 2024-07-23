package com.eul4.model.player;

import com.eul4.scoreboard.TownScoreboard;

public interface VanillaPlayer extends
		PluginPlayer,
		PhysicalPlayer,
		TownPerformer.Reincarnate,
		SpawnPerformer.Teleport,
		PluginScoreboardPlayer
{
	@Override
	TownScoreboard getScoreboard();
}
