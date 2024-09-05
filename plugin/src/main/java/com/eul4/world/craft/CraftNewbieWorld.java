package com.eul4.world.craft;

import com.eul4.type.PluginWorldType;
import com.eul4.world.NewbieWorld;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class CraftNewbieWorld extends CraftNewbieLevel implements NewbieWorld
{
	private final Location spawnLocation = new Location(world, 0.5D, 70.5D, 0.5D, 270.0F, 0.0F);
	
	public CraftNewbieWorld(World world)
	{
		super(world);
	}
	
	@Override
	public PluginWorldType getWorldType()
	{
		return PluginWorldType.NEWBIE_WORLD;
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return spawnLocation.clone();
	}
	
	@Override
	public int getNearSpawnRadius()
	{
		return 128;
	}
	
	@Override
	public boolean isSafeZone(Location location)
	{
		return location.getWorld() == world
				&& Math.abs(location.getX()) <= 5.0D
				&& Math.abs(location.getZ()) <= 5.0D;
	}
	
	@Override
	public boolean isSpawn(Location location)
	{
		return location.getWorld() == world
				&& Math.abs(location.getX()) <= 10.0
				&& Math.abs(location.getZ()) <= 10.0;
	}
}
