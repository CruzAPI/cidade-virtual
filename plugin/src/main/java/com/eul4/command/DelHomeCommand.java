package com.eul4.command;

import com.eul4.Main;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class DelHomeCommand implements TabExecutor
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
		if (!(commandSender instanceof Player player))
		{
			return true;
		}

		String homeName = (args.length > 0) ? args[0] : "default";
		String playerUUID = player.getUniqueId().toString();
		String path = "homes." + playerUUID + "." + homeName;

		if (plugin.getConfig().contains(path))
		{
			plugin.getConfig().set(path, null);
			plugin.saveConfig();
			player.sendMessage("§aCasa " + homeName + " deletada com sucesso.");
		}
		else
		{
			player.sendMessage("§cA casa " + homeName + " não foi encontrada.");
		}

		return true;
	}
}
