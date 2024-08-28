package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.i18n.Messageable;
import com.eul4.common.model.console.CraftConsole;
import com.eul4.common.model.player.CommonPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.eul4.common.i18n.CommonMessage.*;

@RequiredArgsConstructor
public class ChatCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "chat";
	public static final String PERMISSION = "command." + COMMAND_NAME;
	public static final Set<String> OFF_ALIASES = Set.of("off", "false");
	public static final Set<String> ON_ALIASES = Set.of("on", "true");
	
	private final Common plugin;
	
	@Getter
	private boolean enabled = true;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(!plugin.getPermissionService().hasPermission(commandSender, PERMISSION))
		{
			return Collections.emptyList();
		}
		
		if(args.length == 1)
		{
			List<String> suggestions = new ArrayList<>();
			
			final String off = "off";
			final String on = "on";
			
			if(enabled && off.startsWith(args[0].toLowerCase()))
			{
				suggestions.add(off);
			}
			else if(!enabled && on.startsWith(args[0].toLowerCase()))
			{
				suggestions.add(on);
			}
			
			return suggestions;
		}
		
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String aliases, String[] args)
	{
		final Messageable messageable = plugin.getMessageableService().getMessageable(commandSender);
		
		if(plugin.getPermissionService().hasPermission(commandSender, PERMISSION))
		{
			messageable.sendMessage(YOU_DO_NOT_HAVE_PERMISSION);
			return false;
		}
		
		if(args.length == 1 && OFF_ALIASES.contains(args[0].toLowerCase()))
		{
			if(!enabled)
			{
				messageable.sendMessage(COMMAND_CHAT_CHAT_ALREADY_DISABLED);
				return false;
			}
			
			enabled = false;
			messageable.sendMessage(COMMAND_CHAT_CHAT_DISABLED);
			return true;
		}
		if(args.length == 1 && ON_ALIASES.contains(args[0].toLowerCase()))
		{
			if(enabled)
			{
				messageable.sendMessage(COMMAND_CHAT_CHAT_ALREADY_ENABLED);
				return false;
			}
			
			enabled = true;
			messageable.sendMessage(COMMAND_CHAT_CHAT_ENABLED);
			return true;
		}
		else
		{
			messageable.sendMessage(COMMAND_CHAT_USAGE, aliases);
			return false;
		}
	}
}