package com.eul4.common.world;

import com.eul4.common.type.player.CommonWorldType;
import com.eul4.common.type.player.PlayerType;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Collection;

public interface CommonWorld
{
	World getWorld();
	CommonWorldType getWorldType();
	Location getSpawnLocation();
	Collection<PlayerType> getAcceptablePlayerTypes();
	PlayerType getDefaultPlayerType();
}
