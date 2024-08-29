package com.eul4.common.service;

import com.eul4.common.Common;
import com.eul4.common.i18n.Messageable;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class MessageableService
{
	private final Common plugin;
	
	public @NotNull Messageable getMessageable(Entity entity)
	{
		if(entity instanceof Player player)
		{
			return plugin.getPlayerManager().get(player);
		}
		
		return plugin.getDeafenMessageable();
	}
	
	public @NotNull Messageable getMessageable(CommandSender commandSender)
	{
		if(commandSender instanceof ConsoleCommandSender)
		{
			return plugin.getConsole();
		}
		else if(commandSender instanceof Player player)
		{
			return plugin.getPlayerManager().get(player);
		}
		
		return plugin.getDeafenMessageable();
	}
}
