package com.eul4.model.craft.player;

import com.eul4.Common;
import com.eul4.Main;
import com.eul4.model.player.CommonPlayer;
import com.eul4.model.player.TownPlayer;
import org.bukkit.entity.Player;

public class CraftTownPlayer extends CraftCommonPlayer implements TownPlayer
{
	private static final int TOWN_RADIUS = 49;
	
	public CraftTownPlayer(Player player, Common plugin)
	{
		super(player, plugin);
	}
	
	public CraftTownPlayer(CommonPlayer oldCommonPlayer)
	{
		super(oldCommonPlayer);
	}
}
