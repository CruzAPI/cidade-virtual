package com.eul4.model.player;

import com.eul4.common.model.player.CommonAdmin;

public interface Admin extends
		CommonAdmin,
		PluginPlayer,
		PhysicalPlayer,
		Invincible,
		TownPerformer.TeleportInside,
		SpawnPerformer.Teleport,
		SetHomePerformer,
		DelHomePerformer,
		HomePerformer.InstantTeleport,
		NewbiePerformer.Teleport,
		RaidPerformer.Teleport
{

}
