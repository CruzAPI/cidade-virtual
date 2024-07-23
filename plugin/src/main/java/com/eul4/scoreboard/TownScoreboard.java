package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CommonScoreboard;

public interface TownScoreboard extends CommonScoreboard
{
	void updateLikesTeam();
	void updateDislikesTeam();
	void updateHardnessTeam();
	
	default void updateLikesAndDislikesTeams()
	{
		updateLikesTeam();
		updateDislikesTeam();
	}
}
