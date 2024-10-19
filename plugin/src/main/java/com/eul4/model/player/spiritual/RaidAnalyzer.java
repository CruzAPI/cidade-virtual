package com.eul4.model.player.spiritual;

import com.eul4.common.model.player.ScoreboardPlayer;
import com.eul4.common.scoreboard.CommonScoreboard;
import com.eul4.hotbar.RaidAnalyzerHotbar;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.Spectator;
import com.eul4.model.player.SpiritualPlayer;
import com.eul4.model.town.Town;
import com.eul4.scoreboard.AnalyzerScoreboard;

import java.util.Optional;

public interface RaidAnalyzer extends PluginPlayer, SpiritualPlayer, Spectator, //TODO: Implemente TownPerformer & SpawnPerformer
		ScoreboardPlayer
{
	void analyzeTown(Town town);
	Town getAnalyzingTown();
	Optional<Town> findAnalyzingTown();
	RaidAnalyzerHotbar getHotbar();
	void attack();
	void reroll();
	void cancel();
	boolean hasSkipped(Town town);
	
	@Override
	AnalyzerScoreboard getScoreboard();
}
