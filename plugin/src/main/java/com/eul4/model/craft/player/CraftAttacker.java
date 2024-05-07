package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.model.player.Attacker;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.PluginPlayerType;
import org.bukkit.entity.Player;

public class CraftAttacker extends CraftPluginPlayer implements Attacker
{
	public CraftAttacker(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	public CraftAttacker(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
	}
	
	@Override
	public PluginPlayer load()
	{
		commonPlayerData.getPlayerData().apply(player);
		return (PluginPlayer) plugin.getPlayerManager().register(player, this, PluginPlayerType.TOWN_PLAYER);
	}
	
	@Override
	public boolean mustSavePlayerData()
	{
		return false;
	}
	
	@Override
	public PluginPlayerType getPluginPlayerType()
	{
		return PluginPlayerType.ATTACKER;
	}
}
