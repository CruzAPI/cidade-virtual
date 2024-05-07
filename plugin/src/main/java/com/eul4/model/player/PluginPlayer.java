package com.eul4.model.player;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.PlayerType;
import com.eul4.model.playerdata.TownPlayerData;
import com.eul4.model.town.Town;
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
	
	PluginPlayerType getPluginPlayerType();
	
	PluginPlayer load();
	
	Class<? extends PluginPlayer> getType();
	
	void setTownPlayerData(TownPlayerData townPlayerData);
}
