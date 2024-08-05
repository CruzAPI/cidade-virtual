package com.eul4.common.scoreboard;

import com.eul4.common.model.player.ScoreboardPlayer;
import org.bukkit.scoreboard.Scoreboard;

public interface CommonScoreboard
{
	ScoreboardPlayer getScoreboardPlayer();
	Scoreboard getBukkitScoreboard();
	
	void registerScores();
	void registerIfNotRegistered();
}
