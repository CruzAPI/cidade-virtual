package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.model.player.CommonPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static com.eul4.common.i18n.CommonMessage.COMMAND_CLEAR_CHAT_USAGE;
import static com.eul4.common.i18n.CommonMessage.YOU_DO_NOT_HAVE_PERMISSION;

@RequiredArgsConstructor
public class ClearChatCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "clear-chat";
	public static final String PERMISSION = "command." + COMMAND_NAME;
	
	private final Common plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
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
		
		final CommonPlayer commonPlayerSender = plugin.getPlayerManager().get(player);
		
		if(!commonPlayerSender.hasPermission(PERMISSION))
		{
			commonPlayerSender.sendMessage(YOU_DO_NOT_HAVE_PERMISSION);
			return false;
		}
		
		if(args.length == 0)
		{
			for(CommonPlayer allCommonPlayer : plugin.getPlayerManager().getAll())
			{
				allCommonPlayer.clearChat();
			}
			
			return true;
		}
		else
		{
			commonPlayerSender.sendMessage(COMMAND_CLEAR_CHAT_USAGE, aliases);
			return false;
		}
	}
}