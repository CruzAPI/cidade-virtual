package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.exception.CommonException;
import com.eul4.common.exception.MaxMuteReachedException;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.wrapper.UUIDHashSet;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

import static com.eul4.common.i18n.CommonMessage.*;

@RequiredArgsConstructor
public class MuteCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "mute";
	public static final int MUTE_LIMIT = 20;
	
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
			
			for(Player player : plugin.getServer().getOnlinePlayers())
			{
				if(playerSender.canSee(player)
						&& playerSender != player
						&& !ignoredPlayers.contains(player.getUniqueId())
						&& player.getName().toLowerCase().startsWith(args[0].toLowerCase()))
				{
					suggestions.add(player.getName());
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
				
				if(offlinePlayer.getUniqueId().equals(commonPlayer.getUniqueId()))
				{
					commonPlayer.sendMessage(COMMAND_MUTE_YOURSELF);
					return false;
				}
				
				if(commonPlayer.getIgnoredPlayers().size() >= MUTE_LIMIT)
				{
					throw new MaxMuteReachedException(MUTE_LIMIT);
				}
				
				commonPlayer.addIgnoredPlayerOrElseThrow(offlinePlayer);
				commonPlayer.sendMessage(COMMAND_MUTE_USER_MUTED, offlinePlayer);
				return true;
			}
			else
			{
				commonPlayer.sendMessage(COMMAND_MUTE_USE, aliases);
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