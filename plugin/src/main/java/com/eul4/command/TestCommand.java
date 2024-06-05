package com.eul4.command;

import com.eul4.Main;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.TownPlayer;
import lombok.RequiredArgsConstructor;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TestCommand implements TabExecutor
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
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(args.length == 0)
		{
			player.sendMessage(plugin.getPlayerManager().get(player).getPlayerType().getInterfaceType().getSimpleName());
		}
		else if(args.length == 1)
		{
			player.sendMessage("frozen=" + pluginPlayer.getTown().isFrozen());
		}
//		else if(args.length == 1)
//		{
//			if(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
//			{
//				boolean test = Boolean.parseBoolean(args[0]);
//				townPlayer.test(test);
//				player.sendMessage("setting test to: " + townPlayer.test());
//			}
//			else
//			{
//				player.sendMessage("You are not a town player.");
//			}
//		}
		else if(args.length == 1)
		{
			final int amount = Integer.parseInt(args[0]);
			
			for(int i = 0; i < amount; i++)
			{
				try
				{
					plugin.getTownManager().getOrCreateNewTown(UUID.randomUUID());
				}
				catch(Exception e)
				{
					throw new RuntimeException(e);
				}
			}
			
			player.sendMessage("Towns created: " + amount);
		}
		else if(args.length == 2)
		{
			TownPlayer townPlayer = (TownPlayer) plugin.getPlayerManager().get(player);
			double hardness = townPlayer.getTown().getHardness();
			double hardnessLimit = townPlayer.getTown().getHardnessLimit();

			DecimalFormat df = new DecimalFormat("0.0#");

			player.sendMessage(df.format(hardness) + "/" + df.format(hardnessLimit));
		}
		else if(args.length == 2)
		{
			Material type = player.getInventory().getItemInMainHand().getType();
			
			Bukkit.broadcastMessage(type + " hardness: " + type.getHardness()
					+ " br: " + type.getBlastResistance()
					+ " isBlock: " + type.isBlock()
					+ " solid: " + type.isSolid());
		}
		else if(args.length == 3)
		{
			player.sendMessage(plugin.getTownManager().getTowns().size() + " towns");
		}
		
		return false;
	}
}
