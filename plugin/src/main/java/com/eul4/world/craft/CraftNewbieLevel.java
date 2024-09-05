package com.eul4.world.craft;

import com.eul4.type.PluginWorldType;
import com.eul4.world.NewbieLevel;
import org.bukkit.World;

public abstract  class CraftNewbieLevel extends CraftPluginWorld implements NewbieLevel
{
	public CraftNewbieLevel(World world)
	{
		super(world);
	}
	
	@Override
	public final PluginWorldType getRelatedOverWorldType()
	{
		return PluginWorldType.NEWBIE_WORLD;
	}
	
	@Override
	public final PluginWorldType getRelatedNetherType()
	{
		return PluginWorldType.NEWBIE_NETHER;
	}
	
	@Override
	public final PluginWorldType getRelatedEndType()
	{
		return PluginWorldType.NEWBIE_END;
	}
}
