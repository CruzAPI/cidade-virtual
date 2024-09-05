package com.eul4.world.craft;

import com.eul4.type.PluginWorldType;
import com.eul4.world.RaidWorld;
import org.bukkit.Location;
import org.bukkit.World;

public class CraftRaidWorld extends CraftRaidLevel implements RaidWorld
{
	private final Location spawnLocation = new Location(world, 0.5D, 70.5D, 0.5D, 270.0F, 0.0F);
	
	public CraftRaidWorld(World world)
	{
		super(world);
		world.getWorldBorder().setCenter(0.5D, 0.5D);
		world.getWorldBorder().setSize(8000.0D * 2.0D);
	}
	
	@Override
	public PluginWorldType getWorldType()
	{
		return PluginWorldType.RAID_WORLD;
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return spawnLocation.clone();
	}
	
	@Override
	public boolean isSafeZone(Location location)
	{
		return location.getWorld() == world
				&& Math.abs(location.getBlockX()) <= 65.0D
				&& Math.abs(location.getBlockZ()) <= 65.0D;
	}
	
	@Override
	public boolean isSpawn(Location location)
	{
		return location.getWorld() == world
				&& Math.abs(location.getX()) <= 100.0
				&& Math.abs(location.getZ()) <= 100.0;
	}
}
