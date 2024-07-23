package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CommonScoreboard;

public interface AnalyzerScoreboard extends CommonScoreboard
{
	void register();
	
	void updateTitle();
	void updateAnalyzingTeam();
	void updateTownHallLevelTeam();
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
		updateTitle();
		updateAnalyzingTeam();
		updateTownHallLevelTeam();
		updateLikesTeam();
		updateDislikesTeam();
		updateHardnessTeam();
	}
}
