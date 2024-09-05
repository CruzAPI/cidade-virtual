package com.eul4.world;

import com.eul4.common.type.player.PlayerType;

import java.util.Collection;
import java.util.List;

import static com.eul4.type.player.PhysicalPlayerType.*;

public interface NewbieLevel extends VanillaLevel, CommonLevel
{
	List<PlayerType> ACCEPTABLE_PLAYER_TYPES = List.of(VANILLA_PLAYER, ADMIN, SPAWN_PLAYER);
	
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
