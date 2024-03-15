package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.model.player.CommonAdmin;
import com.eul4.common.model.player.CommonPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class AdminCommand implements TabExecutor
{
	private final Common plugin;
	
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
			@NotNull Command command,
			@NotNull String s,
			@NotNull String[] strings)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender commandSender,
			@NotNull Command command,
			@NotNull String s,
			@NotNull String[] strings)
	{
		if(!(commandSender instanceof Player player))
		{
			return true;
		}
		
		final CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		final CommonPlayer newCommonPlayer;
		
		if(commonPlayer instanceof CommonAdmin)
		{
			newCommonPlayer = plugin.getPlayerManager().reregister(commonPlayer, plugin.getDefaultCommonPlayerType());
		}
		else
		{
			newCommonPlayer = plugin.getPlayerManager().reregister(commonPlayer, plugin.getDefaultCommonAdminPlayerType());
		}
		
		newCommonPlayer.reset();
		
		return true;
	}
}
