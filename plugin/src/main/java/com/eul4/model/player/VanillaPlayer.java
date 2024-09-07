package com.eul4.model.player;

public interface VanillaPlayer extends
		PhysicalPlayer,
		TownPerformer.ChannelReincarnation,
		SpawnPerformer.ChannelReincarnation,
		TownScoreboardPlayer,
		InitialScoreboardPlayer,
		SetHomePerformer,
		DelHomePerformer,
		HomePerformer.Channeling,
		Channeler,
		NewbiePerformer.ChannelReincarnation,
		RaidPerformer.ChannelReincarnation
{

}
