package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.TownPlayer;
import com.eul4.scoreboard.CraftTownScoreboard;
import com.eul4.scoreboard.TownScoreboard;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.type.player.PluginPlayerType;
import com.eul4.type.player.SpiritualPlayerType;
import com.eul4.world.TownWorld;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class CraftTownPlayer extends CraftPhysicalPlayer implements TownPlayer
{
	private final transient PluginPlayerType lastPlayerType;
	private final TownScoreboard townScoreboard = new CraftTownScoreboard(this);
	
	public CraftTownPlayer(Player player, Main plugin)
	{
		super(player, plugin);
		this.lastPlayerType = null;
	}
	
	public CraftTownPlayer(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
		this.lastPlayerType = pluginPlayer.getPlayerType();
	}
	
	@Override
	public void reset()
	{
		super.reset();
		player.setGameMode(GameMode.SURVIVAL);
		
		if(!(getCommonWorld() instanceof TownWorld) && hasTown())
		{
			teleportToTownHall();
		}
		
		if(hasTown())
		{
			townScoreboard.registerScores();
		}
	}
	
	@Override
	public boolean test()
	{
		return getTownPlayerData().isTest();
	}
	
	@Override
	public void test(boolean test)
	{
		getTownPlayerData().setTest(test);
	}
	
	@Override
	public boolean mustSavePlayerData()
	{
		return true;
	}
	
	@Override
	public PhysicalPlayerType getPlayerType()
	{
		return PhysicalPlayerType.TOWN_PLAYER;
	}
	
	@Override
	public PluginPlayer load()
	{
		PluginPlayer pluginPlayer = super.load();
		if(!hasTown())
		{
			if(lastPlayerType == PhysicalPlayerType.ADMIN)
			{
				return (PluginPlayer) plugin.getPlayerManager().register(this, lastPlayerType);
			}
			else
			{
				return (PluginPlayer) plugin.getPlayerManager().register(this, PhysicalPlayerType.VANILLA_PLAYER);
			}
		}
		
		if(getTown().isUnderAttack())
		{
			return (PluginPlayer) plugin.getPlayerManager().register(this, SpiritualPlayerType.RAID_SPECTATOR);
		}
		
		return pluginPlayer;
	}
	
	@Override
	public PluginPlayer reload()
	{
		return load(); //TODO is it?
	}
	
	@Override
	public void onStartingTownAttack()
	{
		sendMessage(PluginMessage.TOWN_ATTACK_ALERT, getTown().getCurrentAttack().getAttacker().getPlayer().displayName());
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().register(this, SpiritualPlayerType.RAID_SPECTATOR);
		pluginPlayer.sendMessage(CommonMessage.GAME_MODE_CHANGED, NamedTextColor.AQUA, CommonMessage.SPECTATOR);
	}
	
	@Override
	public void performTown()
	{
		teleportToTownHall();
	}
	
	@Override
	public TownScoreboard getScoreboard()
	{
		return townScoreboard;
	}
	
	@Override
	public void onTownCreate()
	{
		super.onTownCreate();
	}
}
