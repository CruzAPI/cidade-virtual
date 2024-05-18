package com.eul4.model.player;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.playerdata.TownPlayerData;
import com.eul4.model.town.Town;
import com.eul4.type.player.PhysicalPlayerType;
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
	
	TownPlayerData getTownPlayerData();
	
	@Override
	PluginPlayerType getPlayerType();
	
	@Override
	PluginPlayer load();
	
	Class<? extends PluginPlayer> getInterfaceType();
	
	void setTownPlayerData(TownPlayerData townPlayerData);
	
	PhysicalPlayerType getReincarnationType();
}
