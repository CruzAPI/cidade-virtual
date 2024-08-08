package com.eul4.model.player;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.playerdata.TownPlayerData;
import com.eul4.model.playerdata.TutorialTownPlayerData;
import com.eul4.model.playerdata.VanillaPlayerData;
import com.eul4.model.town.Town;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.type.player.PluginPlayerType;
import org.bukkit.Location;

import java.util.Map;
import java.util.Optional;

public interface PluginPlayer extends CommonPlayer
{
	Main getPlugin();
	
	TownPlayerData getTownPlayerData();
	TutorialTownPlayerData getTutorialTownPlayerData();
	VanillaPlayerData getVanillaPlayerData();
	
	@Override
	PluginPlayerType getPlayerType();
	
	@Override
	PluginPlayer load();
	
	@Override
	PluginPlayer reload();
	
	Class<? extends PluginPlayer> getInterfaceType();
	
	void setTownPlayerData(TownPlayerData townPlayerData);
	void setTutorialTownPlayerData(TutorialTownPlayerData tutorialTownPlayerData);
	void setVanillaPlayerData(VanillaPlayerData vanillaPlayerData);
	
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
