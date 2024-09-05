package com.eul4.common.type.player;

import com.eul4.common.i18n.TranslatableMessage;
import com.eul4.common.world.CommonWorld;
import org.bukkit.World;

public interface CommonWorldType
{
	CommonWorld getInstance();
	TranslatableMessage getLabel();
	
	default World getWorld()
	{
		return getInstance().getWorld();
	}
}
