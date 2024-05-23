package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.exception.TownNotFoundException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
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
		player.setGameMode(GameMode.SURVIVAL);
		
		if(!(getCommonWorld() instanceof TownWorld) && hasTown())
		{
			plugin.getLogger().info("[TownPlayerReset] from=" + player.getWorld() + " to=TOWNHALL");//TODO
			teleportToTownHall();
		}
	}
	
	@Override
	public Town getTown()
	{
		return plugin.getTownManager().getTown(player.getUniqueId());
	}
	
	@Override
	public boolean hasTown()
	{
		return getTown() != null;
	}
	
	@Override
	public Structure getMovingStructure()
	{
		return townPlayerData.getMovingStructure();
	}
	
	@Override
	public void setMovingStructure(Structure structure)
	{
		townPlayerData.setMovingStructure(structure);
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
}
