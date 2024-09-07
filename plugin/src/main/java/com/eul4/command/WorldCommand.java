package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.world.CommonWorld;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.town.Town;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static com.eul4.i18n.PluginMessage.*;

@RequiredArgsConstructor
public class WorldCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "world";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME, "mundo" };
	
	private final Main plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String aliases, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return false;
		}
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		CommonWorld commonWorld = pluginPlayer.getCommonWorld();
		
		if(args.length == 0)
		{
			if(commonWorld == null)
			{
				pluginPlayer.sendMessage(COMMAND_WORLD_UNKNOWN_WORLD);
				return false;
			}
			
			pluginPlayer.sendMessage(COMMAND_WORLD_$LABEL, commonWorld.getWorldType().getLabel());
		}
		else
		{
			pluginPlayer.sendMessage(COMMAND_WORLD_USE_$ALIASES, aliases);
		}
		
		return true;
	}
}
