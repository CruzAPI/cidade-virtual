package com.eul4.world;

import org.bukkit.Location;

public interface HomeableLevel extends PluginLevel
{
	default int getNearSpawnRadius()
	{
		return 0;
	}
	
	default boolean isNearSpawn(Location location)
	{
		return location.getWorld() == location.getWorld()
				&& Math.abs(location.getX()) <= getNearSpawnRadius()
				&& Math.abs(location.getZ()) <= getNearSpawnRadius();
	}
}
