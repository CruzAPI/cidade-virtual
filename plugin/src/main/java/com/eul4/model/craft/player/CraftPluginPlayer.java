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
	
	protected CraftPluginPlayer(Player player, Main plugin)
	{
		super(player, plugin);
		this.plugin = plugin;
	}
	
	protected CraftPluginPlayer(PluginPlayer pluginPlayer)
	{
		super(pluginPlayer);
		this.plugin = pluginPlayer.getPlugin();
	}
	
	@Override
	public Main getPlugin()
	{
		return plugin;
	}
}
