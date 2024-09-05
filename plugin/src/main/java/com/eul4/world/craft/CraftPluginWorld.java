package com.eul4.world.craft;

import com.eul4.common.world.craft.CraftCommonWorld;
import com.eul4.world.PluginLevel;
import org.bukkit.Location;
import org.bukkit.World;

public abstract class CraftPluginWorld extends CraftCommonWorld implements PluginLevel
{
	public CraftPluginWorld(World world)
	{
		super(world);
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return null;
	}
}
