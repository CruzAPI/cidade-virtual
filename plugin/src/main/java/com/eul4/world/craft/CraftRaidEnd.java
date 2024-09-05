package com.eul4.world.craft;

import com.eul4.type.PluginWorldType;
import com.eul4.world.RaidEnd;
import org.bukkit.World;

public class CraftRaidEnd extends CraftRaidLevel implements RaidEnd
{
	public CraftRaidEnd(World world)
	{
		super(world);
		world.getWorldBorder().setCenter(0.5D, 0.5D);
		world.getWorldBorder().setSize(8000.0D * 2.0D);
	}
	
	@Override
	public PluginWorldType getWorldType()
	{
		return PluginWorldType.RAID_END;
	}
}
