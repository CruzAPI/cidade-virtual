package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CommonScoreboard;
import com.eul4.model.player.PluginScoreboardPlayer;

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
	
	default void updateAll()
	{
		if(!getScoreboardPlayer().hasTown())
		{
			return;
		}
		
		updateLikesTeam();
		updateDislikesTeam();
		updateHardnessTeam();
	}
	
	@Override
	PluginScoreboardPlayer getScoreboardPlayer();
}
