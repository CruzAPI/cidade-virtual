package com.eul4.common.service;

import com.eul4.common.Common;
import com.eul4.common.i18n.MessageArgs;
import com.eul4.common.i18n.Messageable;
import com.eul4.common.i18n.TranslatableMessage;
import com.eul4.common.model.player.CommonPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class MessageableService
{
	private final Common plugin;
	
	public @NotNull Messageable getMessageable(@Nullable Entity entity)
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
	
	public List<Messageable> getMessageables()
	{
		List<Messageable> messageables = new ArrayList<>();
		
		messageables.add(plugin.getConsole());
		messageables.addAll(plugin.getPlayerManager().getAll());
		
		return messageables;
	}
	
	public void broadcastMessage(MessageArgs messageArgs, Consumer<Player> bonusAction)
	{
		broadcastMessage(messageArgs, null, bonusAction);
	}
	
	public void broadcastMessage
	(
		MessageArgs messageArgs,
		@Nullable String permission,
		@Nullable Consumer<Player> bonusAction
	)
	{
		plugin.getConsole().sendMessage(messageArgs);
		
		for(CommonPlayer commonPlayer : plugin.getPlayerManager().getAll())
		{
			if(permission == null || commonPlayer.hasPermission(permission))
			{
				commonPlayer.sendMessage(messageArgs);
				bonusAction.accept(commonPlayer.getPlayer());
			}
		}
	}
}
