package com.eul4.model.player.physical;

import com.eul4.model.player.*;
import com.eul4.model.player.performer.*;

public interface SpawnPlayer extends PhysicalPlayer,
		TownPerformer.Reincarnate,
		SpawnPerformer.Teleport,
		TownScoreboardPlayer,
		InitialScoreboardPlayer,
		Protectable,
		DelHomePerformer,
		HomePerformer.InstantTeleport,
		NewbiePerformer.Teleport,
		RaidPerformer.Teleport,
		PayPerformer
{
	PluginPlayer removeProtection();
}
