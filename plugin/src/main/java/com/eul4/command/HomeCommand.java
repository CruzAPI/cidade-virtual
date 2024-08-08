package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.model.player.HomePerformer;
import com.eul4.model.player.PluginPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class HomeCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "home";
	
	private final Main plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return Collections.emptyList();
		}
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(pluginPlayer instanceof HomePerformer homePerformer)
		{
			return homePerformer.onHomeTabComplete(args);
		}
		else
		{
			return Collections.emptyList();
		}
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String aliases, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return false;
		}
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(pluginPlayer instanceof HomePerformer homePerformer)
		{
			return homePerformer.performHome(args);
		}
		else
		{
			pluginPlayer.sendMessage(CommonMessage.CAN_NOT_PERFORM);
			return false;
		}
	}
}
