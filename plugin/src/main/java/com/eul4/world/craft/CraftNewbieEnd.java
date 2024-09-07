package com.eul4.world.craft;

import com.eul4.type.PluginWorldType;
import com.eul4.world.NewbieEnd;
import org.bukkit.World;

public class CraftNewbieEnd extends CraftNewbieLevel implements NewbieEnd
{
	public CraftNewbieEnd(World world)
	{
		super(world);
	}
	
	@Override
	public PluginWorldType getWorldType()
	{
		return PluginWorldType.NEWBIE_END;
	}
}
