package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CommonScoreboard;
import com.eul4.model.player.TownScoreboardPlayer;

public interface TownScoreboard extends CommonScoreboard
{
	void registerScores();
	
	void updateTitle();
	void updateLikesTeam();
	void updateDislikesTeam();
	void updateCrownsTeam();
	void updateHardnessTeam();
	void updateFooterTeam();
	
	default void updateLikesAndDislikesTeams()
	{
		updateLikesTeam();
		updateDislikesTeam();
	}
	
	default void updateAll()
	{
		updateTitle();
		
		updateLikesTeam();
		updateDislikesTeam();
		updateCrownsTeam();
		updateHardnessTeam();
		updateFooterTeam();
	}
	
	@Override
	TownScoreboardPlayer getScoreboardPlayer();
}
