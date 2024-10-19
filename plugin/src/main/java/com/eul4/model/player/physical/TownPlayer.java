package com.eul4.model.player.physical;

import com.eul4.model.player.Invincible;
import com.eul4.model.player.PhysicalPlayer;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.TownScoreboardPlayer;
import com.eul4.model.player.performer.*;
import com.eul4.model.town.Town;
import com.eul4.scoreboard.TownScoreboard;

public interface TownPlayer extends PluginPlayer, PhysicalPlayer, Invincible,
		TownPerformer.TeleportInside, SpawnPerformer.Reincarnate, BuyStructurePerformer, TownScoreboardPlayer, DelHomePerformer,
		HomePerformer.InstantTeleport,
		NewbiePerformer.Reincarnate,
		RaidPerformer.Reincarnate,
		PayPerformer
{
	Town getTown();
	boolean hasTown();
	
	void test(boolean test);
	boolean test();
	
	@Override
	TownScoreboard getScoreboard();
}
