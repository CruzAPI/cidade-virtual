package com.eul4.model.player;

import com.eul4.hotbar.RaidAnalyzerHotbar;
import com.eul4.model.town.Town;

public interface RaidAnalyzer extends PluginPlayer, SpiritualPlayer, Spectator //TODO: Implemente TownPerformer & SpawnPerformer
{
	void analyzeTown(Town town);
	Town getAnalyzingTown();
	RaidAnalyzerHotbar getHotbar();
	void attack();
	void reroll();
	void cancel();
	boolean hasSkipped(Town town);
}
