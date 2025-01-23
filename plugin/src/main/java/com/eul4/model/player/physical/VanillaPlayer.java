package com.eul4.model.player.physical;

import com.eul4.model.player.Channeler;
import com.eul4.model.player.InitialScoreboardPlayer;
import com.eul4.model.player.PhysicalPlayer;
import com.eul4.model.player.TownScoreboardPlayer;
import com.eul4.model.player.performer.*;

public interface VanillaPlayer extends PhysicalPlayer,
		TownPerformer.ChannelReincarnation,
		SpawnPerformer.ChannelReincarnation, TownScoreboardPlayer, InitialScoreboardPlayer,
		SetHomePerformer, DelHomePerformer,
		HomePerformer.Channeling, Channeler,
		NewbiePerformer.ChannelReincarnation,
		RaidPerformer.ChannelReincarnation,
		PayPerformer
{

}
