package com.eul4.command;

import com.eul4.Main;
import com.eul4.i18n.BroadcastRichMessage;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.wrapper.BroadcastHashSet;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class MuteBroadcastCommand implements TabExecutor
{
	private static final String COMMAND_NAME = "mutebroadcast";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME };
	
	private final Main plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return Collections.emptyList();
		}
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		BroadcastHashSet mutedBroadcasts = pluginPlayer.getPluginPlayerData().getMutedBroadcasts();
		
		List<String> suggestions = new ArrayList<>();
		
		if(args.length == 1)
		{
			for(BroadcastRichMessage broadcastRichMessage : BroadcastRichMessage.values())
			{
				if(!mutedBroadcasts.contains(broadcastRichMessage) && broadcastRichMessage.name().startsWith(args[0].toUpperCase()))
				{
					suggestions.add(broadcastRichMessage.name());
				}
			}
		}
		
		return suggestions;
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String aliases, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return false;
		}
		
		final PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(args.length == 1)
		{
			try
			{
				BroadcastRichMessage broadcastRichMessage = BroadcastRichMessage.valueOf(args[0].toUpperCase());
				
				if(pluginPlayer.getPluginPlayerData().getMutedBroadcasts().add(broadcastRichMessage))
				{
					pluginPlayer.sendMessage(PluginMessage.COMMAND_MUTEBROADCAST_BROADCAST_MUTED);
					return true;
				}
				else
				{
					pluginPlayer.sendMessage(PluginMessage.COMMAND_MUTEBROADCAST_BROADCAST_ALREADY_MUTED);
					return false;
				}
			}
			catch(IllegalArgumentException e)
			{
				pluginPlayer.sendMessage(PluginMessage.COMMAND_MUTEBROADCAST_BROADCAST_NOT_FOUND, args[0]);
				return false;
			}
		}
		else
		{
			pluginPlayer.sendMessage(PluginMessage.COMMAND_MUTEBROADCAST_USE_$ALIASES, aliases);
			return false;
		}
	}
}
