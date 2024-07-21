package com.eul4.common.model.player;

import static com.eul4.common.i18n.CommonMessage.SCOREBOARD_DISABLED;
import static com.eul4.common.i18n.CommonMessage.SCOREBOARD_ENABLED;

public interface ScoreboardPerformer extends CommonPlayer
{
	default boolean performScoreboard()
	{
		sendMessage(toggleScoreboard() ? SCOREBOARD_ENABLED : SCOREBOARD_DISABLED);
		return true;
	}
	
	default boolean isScoreboardEnabled()
	{
		return getCommonPlayerData().isScoreboardEnabled();
	}
	
	default void setScoreboardEnabled(final boolean scoreboardEnabled)
	{
		getCommonPlayerData().setScoreboardEnabled(scoreboardEnabled);
		resetScoreboard();
	}
	
	default boolean toggleScoreboard()
	{
		setScoreboardEnabled(!isScoreboardEnabled());
		return isScoreboardEnabled();
	}
}
