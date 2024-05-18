package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.model.player.Attacker;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.type.player.SpiritualPlayerType;
import org.bukkit.entity.Player;

public class CraftAttacker extends CraftSpiritualPlayer implements Attacker
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
		return (PluginPlayer) plugin.getPlayerManager().register(player, this, PhysicalPlayerType.TOWN_PLAYER);
	}
	
	@Override
	public boolean mustSavePlayerData()
	{
		return false;
	}
	
	@Override
	public SpiritualPlayerType getPlayerType()
	{
		return SpiritualPlayerType.ATTACKER;
	}
}
