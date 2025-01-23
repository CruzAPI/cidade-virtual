package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CommonScoreboard;

public interface AnalyzerScoreboard extends CommonScoreboard
{
	void updateTitle();
	
	void updatePlayerStatusTeam();
	void updateTownHallLevelTeam();
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
		
		updatePlayerStatusTeam();
		updateTownHallLevelTeam();
		updateLikesTeam();
		updateDislikesTeam();
		updateCrownsTeam();
		updateHardnessTeam();
		updateFooterTeam();
	}
}
