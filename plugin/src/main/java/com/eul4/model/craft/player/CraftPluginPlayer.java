package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.common.Common;
import com.eul4.common.model.player.craft.CraftCommonPlayer;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.player.PluginPlayer;
import org.bukkit.entity.Player;

public class CraftPluginPlayer extends CraftCommonPlayer implements PluginPlayer
{
	protected final Main plugin;
	
	public CraftPluginPlayer(Player player, Common plugin)
	{
		super(player, plugin);
		this.plugin = (Main) plugin;
	}
	
	public CraftPluginPlayer(CommonPlayer oldCommonPlayer)
	{
		super(oldCommonPlayer);
		this.plugin = (Main) oldCommonPlayer.getPlugin();
	}
	
	@Override
	public Main getPlugin()
	{
		return plugin;
	}
}
