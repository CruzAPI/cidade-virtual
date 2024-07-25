package com.eul4.model.player;

public interface SpawnPlayer extends
		PhysicalPlayer,
		TownPerformer.Reincarnate,
		SpawnPerformer.Teleport,
		TownScoreboardPlayer,
		Invincible
{
	PluginPlayer removeProtection();
}
