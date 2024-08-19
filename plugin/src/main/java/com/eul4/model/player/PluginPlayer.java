package com.eul4.model.player;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.playerdata.PluginPlayerData;
import com.eul4.model.playerdata.TownPlayerData;
import com.eul4.model.playerdata.TutorialTownPlayerData;
import com.eul4.model.playerdata.VanillaPlayerData;
import com.eul4.model.town.Town;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.type.player.PluginPlayerType;
import com.eul4.wrapper.Tag;

import java.util.Optional;
import java.util.TreeSet;

public interface PluginPlayer extends CommonPlayer
{
	Main getPlugin();
	
	TownPlayerData getTownPlayerData();
	TutorialTownPlayerData getTutorialTownPlayerData();
	VanillaPlayerData getVanillaPlayerData();
	PluginPlayerData getPluginPlayerData();
	
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
	void setPluginPlayerData(PluginPlayerData pluginPlayerData);
	
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
	
	TreeSet<Tag> getTags();
	Tag getTag();
	Tag getFreshTag();
	void setTag(Tag tag);
	boolean isValidTag();
	boolean hasTag();
	Tag getBestTag();
	void refreshTag();
	boolean isTagHidden();
	boolean isTagShown();
	void hideTag();
	void showTag();
}
