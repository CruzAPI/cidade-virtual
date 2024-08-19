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

import static com.eul4.common.i18n.CommonMessage.*;

@RequiredArgsConstructor
public class DisableChatCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "disable-chat";
	
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
		
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(args.length == 0)
		{
			if(!commonPlayer.isChatEnabled())
			{
				commonPlayer.sendMessage(COMMAND_DISABLE_CHAT_ALREADY_DISABLED);
				return false;
			}
			
			commonPlayer.setChatEnabled(false);
			commonPlayer.sendMessage(COMMAND_DISABLE_CHAT_DISABLED);
			return true;
		}
		else
		{
			commonPlayer.sendMessage(COMMAND_DISABLE_CHAT_USE, aliases);
			return false;
		}
	}
}