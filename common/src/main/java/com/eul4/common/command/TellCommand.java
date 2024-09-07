package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.event.TellEvent;
import com.eul4.common.exception.CommonException;
import com.eul4.common.exception.PlayerNotFoundException;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.listener.CommonChatListener;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.util.CommonMessageUtil;
import com.eul4.common.util.ComponentUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.eul4.common.i18n.CommonMessage.*;

@RequiredArgsConstructor
public class TellCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "tell";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME, "msg", "w" };
	
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
			
			for(Player player : plugin.getServer().getOnlinePlayers())
			{
				if(playerSender.canSee(player)
						&& playerSender != player
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
		if(!(commandSender instanceof Player playerSender))
		{
			return false;
		}
		
		CommonPlayer commonPlayerSender = plugin.getPlayerManager().get(playerSender);
		
		try
		{
			if(!commonPlayerSender.isTellEnabled())
			{
				commonPlayerSender.sendMessage(COMMAND_TELL_YOU_DISABLED_TELL);
				return false;
			}
			
			if(args.length >= 1)
			{
				Player playerTarget = plugin.getOnlinePlayerOrElseThrow(args[0]);
				
				if(playerSender == playerTarget)
				{
					commonPlayerSender.sendMessage(COMMAND_TELL_YOURSELF);
					return false;
				}
				
				if(!playerSender.canSee(playerTarget))
				{
					throw new PlayerNotFoundException(args[0]);
				}
				
				if(args.length > 1)
				{
					String[] messageArgs = Arrays.copyOfRange(args, 1, args.length);
					CommonPlayer commonPlayerTarget = plugin.getPlayerManager().get(playerTarget);
					return executeTell(commonPlayerSender, commonPlayerTarget, messageArgs);
				}
				else
				{
					commonPlayerSender.sendMessage(COMMAND_TELL_USE_$ALIASES_$PLAYER_NAME, aliases, playerTarget.getName());
					return false;
				}
			}
			else
			{
				commonPlayerSender.sendMessage(COMMAND_TELL_USE_$ALIASES, aliases);
				return false;
			}
		}
		catch(CommonException e)
		{
			commonPlayerSender.sendMessage(e.getMessageArgs());
			return false;
		}
	}
	
	public boolean executeTell(final CommonPlayer commonPlayerSender,
			final CommonPlayer commonPlayerTarget,
			final String[] args)
	{
		final Player playerSender = commonPlayerSender.getPlayer();
		final Player playerTarget = commonPlayerTarget.getPlayer();
		final String message = CommonMessageUtil.joinMessage(args);
		
		if(commonPlayerSender.hasIgnored(playerTarget))
		{
			commonPlayerSender.sendMessage(COMMAND_TELL_CAN_NOT_TELL_IGNORED_PLAYER);
			return false;
		}
		
		if(!commonPlayerSender.hasPermission(CommonChatListener.PERMISSION_CHAT_BYPASS)
				&& (!commonPlayerTarget.isTellEnabled()
				|| commonPlayerTarget.hasIgnored(playerSender)))
		{
			commonPlayerSender.sendMessage(COMMAND_DISABLE_TELL_THIS_PLAYER_DISABLED_TELL);
			return false;
		}
		
		if(new TellEvent(commonPlayerSender, commonPlayerTarget).callEvent())
		{
			commonPlayerSender.setLastReplied(playerTarget);
			commonPlayerTarget.setLastReplied(playerSender);
			commonPlayerSender.sendMessage(COMMAND_TELL_$SENDER_$RECEIVER_$MESSAGE, playerSender, playerTarget, message);
			commonPlayerTarget.sendMessage(COMMAND_TELL_$SENDER_$RECEIVER_$MESSAGE, playerSender, playerTarget, message);
			return true;
		}
		else
		{
			commonPlayerSender.sendMessage(COMMAND_TELL_BUSY_$PLAYER, playerTarget);
			return false;
		}
	}
}