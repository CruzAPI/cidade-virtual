package com.eul4.command;

import com.eul4.Main;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class TutorialCommand implements TabExecutor
{
	public static String COMMAND_NAME = "tutorial";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME };
	
	private final Main plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
	{
		if(!(commandSender instanceof Player player))
		{
			return false;
		}
		
//		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
//		pluginPlayer = (PluginPlayer) plugin.getPlayerManager().register(pluginPlayer, PhysicalPlayerType.TUTORIAL_TOWN_PLAYER);
//
		return true;
	}
}
