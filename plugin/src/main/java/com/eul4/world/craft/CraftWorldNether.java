package com.eul4.world.craft;

import com.eul4.type.PluginWorldType;
import com.eul4.world.WorldNether;
import org.bukkit.World;

public class CraftWorldNether extends CraftPluginWorld implements WorldNether
{
	public CraftWorldNether(World world)
	{
		super(world);
	}
	
	@Override
	public PluginWorldType getWorldType()
	{
		return PluginWorldType.WORLD_NETHER;
	}
}
