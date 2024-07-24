package com.eul4.world.craft;

import com.eul4.type.PluginWorldType;
import com.eul4.world.OverWorld;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

public class CraftOverWorld extends CraftPluginWorld implements OverWorld
{
	public CraftOverWorld(World world)
	{
		super(world);
	}
	
	@Override
	public PluginWorldType getWorldType()
	{
		return PluginWorldType.OVER_WORLD;
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return world.getBlockAt(0, 0, 0)
				.getLocation()
				.toHighestLocation()
				.getBlock()
				.getRelative(BlockFace.UP)
				.getLocation()
				.toCenterLocation();
	}
	
	@Override
	public boolean isSafeZone(Location location)
	{
		return location.getWorld() == world
				&& Math.abs(location.getBlockX()) <= 65
				&& Math.abs(location.getBlockZ()) <= 65;
	}
	
	@Override
	public boolean isSpawn(Location location)
	{
		return isSpawn(location.getBlock());
	}
	
	@Override
	public boolean isSpawn(Block block)
	{
		return isSpawn(block.getState());
	}
	
	@Override
	public boolean isSpawn(BlockState blockState)
	{
		return blockState.getWorld() == world
				&& Math.abs(blockState.getX()) <= 100
				&& Math.abs(blockState.getZ()) <= 100;
	}
}
