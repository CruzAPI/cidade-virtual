package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.event.WorldSaveOrStopEvent;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.i18n.Messageable;
import com.eul4.common.model.console.CraftConsole;
import com.eul4.common.model.player.CommonPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class StopCommand implements TabExecutor
{
	private final Common plugin;
	
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
			@NotNull Command command,
			@NotNull String alias,
			@NotNull String[] args)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender commandSender,
			@NotNull Command command,
			@NotNull String alias,
			@NotNull String[] args)
	{
		Messageable messageable;
		
		if(commandSender instanceof Player player)
		{
			CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
			messageable = commonPlayer;
			
			if(!player.isOp())
			{
				messageable.sendMessage(CommonMessage.YOU_DO_NOT_HAVE_PERMISSION);
				return true;
			}
		}
		else if(commandSender instanceof ConsoleCommandSender consoleCommandSender)
		{
			messageable = new CraftConsole(consoleCommandSender);
		}
		else
		{
			return true;
		}
		
		for(World world : plugin.getServer().getWorlds())
		{
			plugin.getServer().getPluginManager().callEvent(new WorldSaveOrStopEvent(world));
		}
		
		plugin.getLogger().info(commandSender.getName() + " stopping server... ");
		plugin.getServer().shutdown();
		
		return false;
	}
}
