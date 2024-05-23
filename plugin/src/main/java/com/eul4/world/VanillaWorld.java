package com.eul4.world;

import com.eul4.common.type.player.PlayerType;

import java.util.Collection;
import java.util.List;

import static com.eul4.type.player.PhysicalPlayerType.ADMIN;
import static com.eul4.type.player.PhysicalPlayerType.VANILLA_PLAYER;

public interface VanillaWorld extends PluginWorld
{
	List<PlayerType> ACCEPTABLE_PLAYER_TYPES = List.of(VANILLA_PLAYER, ADMIN);
	
	@Override
	default Collection<PlayerType> getAcceptablePlayerTypes()
	{
		return ACCEPTABLE_PLAYER_TYPES;
	}
	
	@Override
	default PlayerType getDefaultPlayerType()
	{
		return VANILLA_PLAYER;
	}
}
