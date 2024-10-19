package com.eul4.model.player.spiritual;

import com.eul4.hotbar.RaidSpectatorHotbar;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.Spectator;
import com.eul4.model.player.SpiritualPlayer;
import com.eul4.model.player.Warder;
import com.eul4.model.player.performer.SpawnPerformer;
import com.eul4.model.player.performer.TownPerformer;

public interface RaidSpectator extends PluginPlayer, SpiritualPlayer, Spectator, Warder,
		TownPerformer.TeleportOutside, SpawnPerformer.Reincarnate
{
	RaidSpectatorHotbar getHotbar();
	
	void defend();
	void vanilla();
}
