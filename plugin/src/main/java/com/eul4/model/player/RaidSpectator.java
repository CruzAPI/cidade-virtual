package com.eul4.model.player;

import com.eul4.hotbar.RaidSpectatorHotbar;

public interface RaidSpectator extends
		PluginPlayer,
		SpiritualPlayer,
		Spectator,
		Warder,
		TownPerformer.TeleportOutside,
		SpawnPerformer.Reincarnation
{
	RaidSpectatorHotbar getHotbar();
	
	void defend();
	void vanilla();
}
