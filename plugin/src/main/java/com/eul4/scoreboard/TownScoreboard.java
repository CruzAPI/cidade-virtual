package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CommonScoreboard;
import com.eul4.model.player.TownScoreboardPlayer;

public interface TownScoreboard extends CommonScoreboard
{
	void registerScores();
	
	void updateLikesTeam();
	void updateDislikesTeam();
	void updateHardnessTeam();
	
	default void updateLikesAndDislikesTeams()
	{
		updateLikesTeam();
		updateDislikesTeam();
	}
	
	default void updateAll()
	{
		updateLikesTeam();
		updateDislikesTeam();
		updateHardnessTeam();
	}
	
	@Override
	TownScoreboardPlayer getScoreboardPlayer();
}
