package com.eul4.world.craft;

import com.eul4.type.PluginWorldType;
import com.eul4.world.OverWorld;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

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
}
