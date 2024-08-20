package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.event.BroadcastReceiveEvent;
import com.eul4.common.i18n.Messageable;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.util.ComponentUtil;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static com.eul4.common.i18n.CommonMessage.*;

@RequiredArgsConstructor
public class BroadcastCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "broadcast";
	public static final String PERMISSION = "command." + COMMAND_NAME;
	
	private final Common plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
	{
		Messageable messageable = plugin.getMessageableService().getMessageable(commandSender);
		
		if(messageable == null)
		{
			return false;
		}
		
		if(!plugin.getPermissionService().hasPermission(commandSender, PERMISSION))
		{
			messageable.sendMessage(YOU_DO_NOT_HAVE_PERMISSION);
			return false;
		}
		
		if(args.length > 0)
		{
			String msg = "";
			
			for(int i = 0; i < args.length; i++)
			{
				msg += args[i] + " ";
			}
			
			msg = msg.substring(0, msg.length() - 1);
			Component component = ComponentUtil.chatInputToComponent(msg);
			
			for(CommonPlayer allCommonPlayer : plugin.getPlayerManager().getAll())
			{
				if(new BroadcastReceiveEvent(allCommonPlayer).callEvent())
				{
					allCommonPlayer.sendMessage(COMMAND_BROADCAST, component);
				}
			}
			
			return true;
		}
		else
		{
			messageable.sendMessage(COMMAND_BROADCAST_USAGE);
			return false;
		}
	}
}
