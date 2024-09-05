package com.eul4.world.craft;

import com.eul4.type.PluginWorldType;
import com.eul4.world.NewbieNether;
import org.bukkit.World;

public class CraftNewbieNether extends CraftNewbieLevel implements NewbieNether
{
	public CraftNewbieNether(World world)
	{
		super(world);
	}
	
	@Override
	public PluginWorldType getWorldType()
	{
		return PluginWorldType.NEWBIE_NETHER;
	}
}
