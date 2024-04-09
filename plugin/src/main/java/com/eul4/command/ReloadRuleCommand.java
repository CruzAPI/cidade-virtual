package com.eul4.command;

import com.eul4.Main;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ReloadRuleCommand implements TabExecutor
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
		
		if(!player.isOp())
		{
			return true;
		}
		
		else if(args.length == 0)
		{
			try
			{
				plugin.reloadRules();
				player.sendMessage("Rules reloaded successfully!");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				player.sendMessage("Failed to reload rules, see console for stack trace.");
			}
		}
		else
		{
			player.sendMessage("Usage: /" + alias);
		}
		
		return false;
	}
}
