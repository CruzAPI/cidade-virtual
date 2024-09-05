package com.eul4.world;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public interface SpawnProtectedLevel extends PluginLevel
{
	boolean isSafeZone(Location location);
	boolean isSpawn(Location location);
	
	default boolean isSpawn(Block block)
	{
		return isSpawn(block.getLocation());
	}
	
	default boolean isSpawn(BlockState blockState)
	{
		return isSpawn(blockState.getLocation());
	}
}
