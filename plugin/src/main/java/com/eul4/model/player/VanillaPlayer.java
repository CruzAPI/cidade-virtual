package com.eul4.model.player;

import com.eul4.scoreboard.TownScoreboard;

public interface VanillaPlayer extends
		PhysicalPlayer,
		TownPerformer.Reincarnate,
		SpawnPerformer.Reincarnation,
		PluginScoreboardPlayer
{
	@Override
	TownScoreboard getScoreboard();
}
