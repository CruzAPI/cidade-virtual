package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.Town;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.eul4.i18n.PluginMessage.COMMAND_TOWN_USAGE;

@RequiredArgsConstructor
public class TownCommand implements TabExecutor
{
	private final Main plugin;
	
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
		if(!(commandSender instanceof Player player))
		{
			return true;
		}
		
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(args.length == 0)
		{
			try
			{
				Town town = plugin.getTownManager().getOrCreateNewTown(player.getUniqueId());
				player.teleport(town.getLocation().add(0.0D, 1.0D, 0.0D));
			}
			catch(CannotConstructException | IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			commonPlayer.sendMessage(COMMAND_TOWN_USAGE, alias);
		}
		
		return false;
	}
	
	
	
}
