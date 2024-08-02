package com.eul4.model.player;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.playerdata.TownPlayerData;
import com.eul4.model.town.Town;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.type.player.PluginPlayerType;

import java.util.Optional;

public interface PluginPlayer extends CommonPlayer
{
	Main getPlugin();
	
	TownPlayerData getTownPlayerData();
	
	@Override
	PluginPlayerType getPlayerType();
	
	@Override
	PluginPlayer load();
	
	@Override
	PluginPlayer reload();
	
	Class<? extends PluginPlayer> getInterfaceType();
	
	void setTownPlayerData(TownPlayerData townPlayerData);
	
	PhysicalPlayerType getReincarnationType();
	
	void onStartingTownAttack();
	
	void onFinishingTownAttack();
	
	boolean teleportToTownHall();
	
	boolean teleportToHighestTownHall();
	
	Optional<Town> findTown();
	
	int getAttackCooldown();
	
	void resetAttackCooldown();
	
	boolean isCritical();
	
	Town getTown();
	
	boolean hasTown();
	
	void onTownCreate();
}
