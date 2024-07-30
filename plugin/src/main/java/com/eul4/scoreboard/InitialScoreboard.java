package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CommonScoreboard;
import com.eul4.model.player.InitialScoreboardPlayer;

public interface InitialScoreboard extends CommonScoreboard
{
	@Override
	InitialScoreboardPlayer getScoreboardPlayer();
}
