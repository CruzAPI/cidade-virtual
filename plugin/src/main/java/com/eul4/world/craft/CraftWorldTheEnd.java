package com.eul4.world.craft;

import com.eul4.type.PluginWorldType;
import com.eul4.world.WorldTheEnd;
import org.bukkit.World;

public class CraftWorldTheEnd extends CraftPluginWorld implements WorldTheEnd
{
	public CraftWorldTheEnd(World world)
	{
		super(world);
	}
	
	@Override
	public PluginWorldType getWorldType()
	{
		return PluginWorldType.WORLD_THE_END;
	}
}
