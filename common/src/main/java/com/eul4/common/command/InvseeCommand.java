package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.i18n.Messageable;
import com.eul4.common.model.player.CommonPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.eul4.common.i18n.CommonMessage.*;

@RequiredArgsConstructor
public class InvseeCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "invsee";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME };
	public static final String PERMISSION = "command." + COMMAND_NAME;
	
	private final Common plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(!(commandSender instanceof Player playerSender)
				|| !plugin.getPermissionService().hasPermission(commandSender, PERMISSION))
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
		if(!(commandSender instanceof Player player))
		{
			return false;
		}
		
		Messageable messageable = plugin.getMessageableService().getMessageable(commandSender);
		
		if(!plugin.getPermissionService().hasPermission(commandSender, PERMISSION))
		{
			messageable.sendMessage(YOU_DO_NOT_HAVE_PERMISSION);
			return false;
		}
		
		if(args.length == 1)
		{
			Player playerTarget = plugin.getServer().getPlayerExact(args[0]);
			
			if(playerTarget == null)
			{
				messageable.sendMessage(EXCEPTION_PLAYER_NOT_FOUND, args[0]);
				return false;
			}
			
			if(player == playerTarget)
			{
				messageable.sendMessage(COMMAND_INVSEE_YOURSELF, args[0]);
				return false;
			}
			
			player.openInventory(playerTarget.getInventory());
			return true;
		}
		else
		{
			messageable.sendMessage(COMMAND_INVSEE_USE_$ALIASES, aliases);
			return false;
		}
	}
}