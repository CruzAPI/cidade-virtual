package com.eul4.model.player;

import com.eul4.hotbar.DefenderSpectatorHotbar;

public interface DefenderSpectator extends
		PluginPlayer,
		SpiritualPlayer,
		Spectator,
		Warder,
		TownPerformer.TeleportOutside, SpawnPerformer.Reincarnate
{
	DefenderSpectatorHotbar getHotbar();
	
	void quit();
	void respawn();
}
