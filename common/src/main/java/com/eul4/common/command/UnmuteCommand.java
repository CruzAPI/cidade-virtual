package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.exception.CommonException;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.wrapper.UUIDHashSet;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.eul4.common.i18n.CommonMessage.*;

@RequiredArgsConstructor
public class UnmuteCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "unmute";
	
	private final Common plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(!(commandSender instanceof Player playerSender))
		{
			return Collections.emptyList();
		}
		
		if(args.length == 1)
		{
			List<String> suggestions = new ArrayList<>();
			
			CommonPlayer commonPlayerSender = plugin.getPlayerManager().get(playerSender);
			UUIDHashSet ignoredPlayers = commonPlayerSender.getIgnoredPlayers();
			
			for(UUID ignoredPlayerUniqueId : ignoredPlayers.getHashSet())
			{
				OfflinePlayer ignoredPlayer = plugin.getServer().getOfflinePlayer(ignoredPlayerUniqueId);
				String name = ignoredPlayer.getName();
				
				if(name != null && name.toLowerCase().startsWith(args[0].toLowerCase()))
				{
					suggestions.add(name);
				}
			}
			
			return suggestions;
		}
		
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String aliases, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return false;
		}
		
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		try
		{
			if(args.length == 1)
			{
				OfflinePlayer offlinePlayer = plugin.getOfflinePlayerIfCachedOrElseThrow(args[0]);
				commonPlayer.removeIgnoredPlayerOrElseThrow(offlinePlayer);
				commonPlayer.sendMessage(COMMAND_UNMUTE_USER_UNMUTED, offlinePlayer);
				return true;
			}
			else
			{
				commonPlayer.sendMessage(COMMAND_UNMUTE_USE, aliases);
				return false;
			}
		}
		catch(CommonException e)
		{
			commonPlayer.sendMessage(e.getMessageArgs());
			return false;
		}
	}
}