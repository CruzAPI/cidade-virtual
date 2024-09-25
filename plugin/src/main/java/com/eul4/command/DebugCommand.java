package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.i18n.Messageable;
import com.eul4.common.world.CommonWorld;
import com.eul4.enums.Rarity;
import com.eul4.model.player.PluginPlayer;
import com.eul4.service.BlockData;
import com.google.common.collect.Multimap;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
		
		}
		else if(args.length == 2 && args[0].equalsIgnoreCase("goto"))
		{
			String worldName = args[1];
			World world = plugin.getServer().getWorld(worldName);
			
			if(world == null)
			{
				player.sendMessage("world not found");
				return false;
			}
			
			CommonWorld commonWorld = plugin.getWorldManager().get(world);
			
			if(commonWorld == null)
			{
				player.sendMessage("common world not found");
				return false;
			}
			
			Location spawnLocation = Optional.ofNullable(commonWorld.getSpawnLocation())
					.orElse(new Location(commonWorld.getWorld(), 0.5D, 70.0D, 0.5D, 0.0F, 0.0F));
			
			player.teleport(spawnLocation);
		}
		else if(args.length == 1 && args[0].equalsIgnoreCase("reloadstructure"))
		{
			for(GeneratedStructure generatedStructure : player.getChunk().getStructures())
			{
				plugin.getStructureRarityListener().rarifyStructure(player.getWorld(), generatedStructure);
			}
		}
		else if(args.length == 1 && args[0].equalsIgnoreCase("chunk"))
		{
//			for(int y = player.getWorld().getMinHeight(); y < player.getWorld().getMaxHeight(); y++)
//			{
//				for(int x = 0; x < 16; x++)
//				{
//					for(int z = 0; z < 16; z++)
//					{
//						plugin.getBlockDataFiler().loadBlockDataOrDefault(player.getChunk().getBlock(x, y, z),
//								() -> BlockData.builder().rarity(Rarity.RARE).build());
//					}
//				}
//			}
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
				player.sendMessage(Bukkit.getTag("blocks", NamespacedKey.minecraft(args[0].toLowerCase()), Material.class).getValues().toString());
			}
		}
		return false;
	}
}
