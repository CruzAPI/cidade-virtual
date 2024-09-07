package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.player.TownPerformer;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static com.eul4.i18n.PluginMessage.COMMAND_TOWN_USAGE;

@RequiredArgsConstructor
public class TownCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "town";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME, "cidade", "casa", "cv" };
	
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
			return false;
		}
		
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(!(commonPlayer instanceof TownPerformer townPerformer))
		{
			commonPlayer.sendMessage(CommonMessage.CAN_NOT_PERFORM);
			return false;
		}
		
		if(args.length == 0)
		{
			townPerformer.performTown();
			return true;
		}
		
		commonPlayer.sendMessage(COMMAND_TOWN_USAGE, alias);
		return false;
	}
}
