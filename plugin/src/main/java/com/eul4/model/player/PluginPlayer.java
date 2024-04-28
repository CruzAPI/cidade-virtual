package com.eul4.model.player;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.CommonPlayerType;
import com.eul4.model.town.Town;
import com.eul4.type.player.PluginCommonPlayerType;
import com.eul4.type.player.PluginPlayerType;

public interface PluginPlayer extends CommonPlayer
{
	Main getPlugin();
	
	default Town getTown()
	{
		return getPlugin().getTownManager().getTown(getUniqueId());
	}
	
	default boolean hasTown()
	{
		return getTown() != null;
	}
	
	PluginCommonPlayerType.Type getLastLoadableCommonPlayerType();
	
	@Override
	PluginCommonPlayerType<? extends PluginPlayer> getCommonPlayerType();
	
	PluginCommonPlayerType<? extends PluginPlayer> getCommonPlayerType();
}
