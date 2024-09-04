package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.i18n.Messageable;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.type.PluginWorldType;
import com.google.common.collect.Multimap;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.level.ServerLevel;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DebugCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "debug";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME };
	
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
		
		if(args.length == 0)
		{
			Bukkit.getLogger().severe(plugin.getServer().getStructureManager().getStructures().toString());
			player.sendMessage(plugin.getServer().getStructureManager().getStructures().toString());
		}
		else if(args.length == 1)
		{
			if(args.length == 1 && args[0].equalsIgnoreCase("toggle-combat-map"))
			{
				player.sendMessage(plugin.getToggleCombatCommand().getCooldownMap().toString());
				return true;
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("item-attributes"))
			{
				player.sendMessage
				(
					Optional
							.of(player.getInventory().getItemInMainHand())
							.map(ItemStack::getItemMeta)
							.map(ItemMeta::getAttributeModifiers)
							.map(Multimap::toString)
							.orElse("null")
				);
			}
			else
			{
			
			}
		}
		return false;
	}
}
