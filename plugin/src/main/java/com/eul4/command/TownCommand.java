package com.eul4.command;

import com.eul4.Main;
import com.eul4.i18n.CommonMessage;
import com.eul4.i18n.PluginMessage;
import com.eul4.i18n.ResourceBundleHandler;
import com.eul4.model.player.CommonPlayer;
import com.eul4.model.player.TownPlayer;
import com.eul4.type.player.PluginPlayerType;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static com.eul4.i18n.CommonMessage.USAGE;
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
		
		}
		else
		{
			commonPlayer.sendMessage(COMMAND_TOWN_USAGE, alias);
		}
		
		return false;
	}
}
