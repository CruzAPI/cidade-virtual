package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.i18n.Messageable;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.type.PluginWorldType;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class DebugCommand implements TabExecutor
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
		Messageable messageable = plugin.getMessageableService().getMessageable(commandSender);
		
		if(messageable == null)
		{
			return false;
		}
		
		if(!plugin.getPermissionService().hasPermission(commandSender, "command.debug"))
		{
			messageable.sendMessage(CommonMessage.YOU_DO_NOT_HAVE_PERMISSION);
			return true;
		}
		
		if(!(commandSender instanceof Player player))
		{
			return true;
		}
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(args.length == 1 && args[0].equalsIgnoreCase("toggle-combat-map"))
		{
			player.sendMessage(plugin.getToggleCombatCommand().getCooldownMap().toString());
		}
		
		return false;
	}
}
