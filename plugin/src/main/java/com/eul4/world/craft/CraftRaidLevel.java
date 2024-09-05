package com.eul4.world.craft;

import com.eul4.type.PluginWorldType;
import com.eul4.world.RaidLevel;
import org.bukkit.World;

public abstract class CraftRaidLevel extends CraftPluginWorld implements RaidLevel
{
	public CraftRaidLevel(World world)
	{
		super(world);
	}
	
	@Override
	public final PluginWorldType getRelatedOverWorldType()
	{
		return PluginWorldType.RAID_WORLD;
	}
	
	@Override
	public final PluginWorldType getRelatedNetherType()
	{
		return PluginWorldType.RAID_NETHER;
	}
	
	@Override
	public final PluginWorldType getRelatedEndType()
	{
		return PluginWorldType.RAID_END;
	}
}
