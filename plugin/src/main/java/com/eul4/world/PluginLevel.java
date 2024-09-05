package com.eul4.world;

import com.eul4.common.world.CommonWorld;
import com.eul4.type.PluginWorldType;

public interface PluginLevel extends CommonWorld
{
	@Override
	PluginWorldType getWorldType();
}
