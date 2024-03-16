package com.eul4.model.craft.player;

import com.eul4.common.Common;
import com.eul4.common.model.craft.player.CraftCommonPlayer;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class CraftTownPlayer extends CraftPluginPlayer implements TownPlayer
{
	public CraftTownPlayer(Player player, Common plugin)
	{
		super(player, plugin);
	}
	
	public CraftTownPlayer(CommonPlayer oldCommonPlayer)
	{
		super(oldCommonPlayer);
	}
	
	@Override
	public void reset()
	{
		player.setGameMode(GameMode.SURVIVAL);
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
}
