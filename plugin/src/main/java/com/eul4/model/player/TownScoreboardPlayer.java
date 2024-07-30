package com.eul4.model.player;

import com.eul4.scoreboard.TownScoreboard;

public interface TownScoreboardPlayer extends PluginScoreboardPlayer
{
	default TownScoreboard getTownScoreboard()
	{
		return getScoreboard() instanceof TownScoreboard townScoreboard ? townScoreboard : null;
	}
}
