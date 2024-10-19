package com.eul4.model.player.spiritual;

import com.eul4.hotbar.DefenderSpectatorHotbar;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.Spectator;
import com.eul4.model.player.SpiritualPlayer;
import com.eul4.model.player.Warder;
import com.eul4.model.player.performer.SpawnPerformer;
import com.eul4.model.player.performer.TownPerformer;

public interface DefenderSpectator extends PluginPlayer, SpiritualPlayer, Spectator, Warder,
		TownPerformer.TeleportOutside, SpawnPerformer.Reincarnate
{
	DefenderSpectatorHotbar getHotbar();
	
	void quit();
	void respawn();
}
