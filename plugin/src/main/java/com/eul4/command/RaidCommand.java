package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.RaidPerformer;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class RaidCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "raid";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME };
	
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
			return true;
		}
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(args.length == 0)
		{
			if(!(pluginPlayer instanceof RaidPerformer raidPerformer))
			{
				pluginPlayer.sendMessage(CommonMessage.CAN_NOT_PERFORM);
				return false;
			}
			
			return raidPerformer.performRaid();
		}
		else
		{
			pluginPlayer.sendMessage(PluginMessage.COMMAND_RAID_USE_$ALIASES, aliases);
			return false;
		}
	}
}
