package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.i18n.CommonRichMessage;
import com.eul4.common.model.player.CommonPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static com.eul4.common.i18n.CommonMessage.*;
import static com.eul4.common.i18n.CommonRichMessage.COMMAND_REPLY_STATUS_$PLAYER_$ALIASES;

@RequiredArgsConstructor
public class ReplyCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "reply";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME, "r", "responder" };
	
	private final Common plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String aliases, String[] args)
	{
		if(!(commandSender instanceof Player playerSender))
		{
			return false;
		}
		
		CommonPlayer commonPlayerSender = plugin.getPlayerManager().get(playerSender);
		
		if(!commonPlayerSender.isTellEnabled())
		{
			commonPlayerSender.sendMessage(COMMAND_TELL_YOU_DISABLED_TELL);
			return false;
		}
		
		OfflinePlayer lastRepliedOfflinePlayer = commonPlayerSender.getLastReplied();
		
		if(lastRepliedOfflinePlayer == null)
		{
			commonPlayerSender.sendMessage(COMMAND_REPLY_NO_CHATTING_$ALIASES, aliases);
			return false;
		}
		
		Player lastReplied = lastRepliedOfflinePlayer.getPlayer();
		
		if(lastReplied == null)
		{
			commonPlayerSender.sendMessage(COMMAND_REPLY_GONE_OFFLINE_$PLAYER, lastRepliedOfflinePlayer);
			return false;
		}
		
		if(args.length == 0)
		{
			commonPlayerSender.sendMessage(COMMAND_REPLY_STATUS_$PLAYER_$ALIASES, lastReplied, aliases);
			return true;
		}
		else
		{
			CommonPlayer commonPlayerTarget = plugin.getPlayerManager().get(lastReplied);
			return plugin.getTellCommand().executeTell(commonPlayerSender, commonPlayerTarget, args);
		}
	}
}