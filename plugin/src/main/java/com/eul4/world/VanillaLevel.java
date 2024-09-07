package com.eul4.world;

import com.eul4.type.PluginWorldType;
import org.bukkit.World;

public interface VanillaLevel extends PluginLevel, HomeableLevel
{
	PluginWorldType getRelatedOverWorldType();
	PluginWorldType getRelatedNetherType();
	PluginWorldType getRelatedEndType();
	
	default PluginWorldType getRelated(World.Environment environment)
	{
		return switch(environment)
		{
			case NORMAL -> getRelatedOverWorldType();
			case NETHER -> getRelatedNetherType();
			case THE_END -> getRelatedEndType();
			default -> null;
		};
	}
}
