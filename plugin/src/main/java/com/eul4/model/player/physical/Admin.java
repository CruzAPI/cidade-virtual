package com.eul4.model.player.physical;

import com.eul4.common.model.player.CommonAdmin;
import com.eul4.model.player.Invincible;
import com.eul4.model.player.PhysicalPlayer;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.performer.*;

public interface Admin extends
		CommonAdmin, PluginPlayer, PhysicalPlayer, Invincible,
		TownPerformer.TeleportInside,
		SpawnPerformer.Teleport,
		SetHomePerformer,
		DelHomePerformer,
		HomePerformer.InstantTeleport,
		NewbiePerformer.Teleport,
		RaidPerformer.Teleport,
		PayPerformer
{

}
