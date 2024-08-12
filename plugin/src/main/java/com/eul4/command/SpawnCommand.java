package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.SpawnPerformer;
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
public class SpawnCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "spawn";
	
	private final Main plugin;
	
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
			@NotNull Command command,
			@NotNull String label,
			@NotNull String[] args)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender,
			@NotNull Command command,
			@NotNull String label,
			@NotNull String[] args)
	{
		if(!(sender instanceof Player player))
		{
			return false;
		}
		
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(!(commonPlayer instanceof SpawnPerformer spawnPerformer))
		{
			commonPlayer.sendMessage(CommonMessage.CAN_NOT_PERFORM);
			return false;
		}
		
		if(args.length == 0)
		{
			spawnPerformer.performSpawn();
			return true;
		}
		
		commonPlayer.sendMessage(PluginMessage.COMMAND_SPAWN_USAGE, label);
		return false;
	}
}
