package com.eul4.world;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public interface OverWorld extends VanillaWorld
{
	int NEAR_SPAWN_RADIUS = 256;
	
	boolean isSafeZone(Location location);
	boolean isSpawn(Location location);
	boolean isSpawn(Block block);
	boolean isSpawn(BlockState blockState);
	boolean isNearSpawn(Location location);
}
