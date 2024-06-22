package com.eul4.command;

import com.eul4.Main;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.TownPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER;

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
			player.sendMessage("frozen=" + pluginPlayer.getTown().isFrozen());
		}
		else if(args.length == 2)
		{
			int damage = Integer.parseInt(args[0]);
			int maxDamage = Integer.parseInt(args[1]);
			
			ItemStack item = player.getInventory().getItemInMainHand();
			
			Bukkit.broadcastMessage("maxItemUseDuration: " + item.getMaxItemUseDuration());
			Bukkit.broadcastMessage("durability: " + item.getDurability());
			
			ItemMeta meta = item.getItemMeta();
			if(meta instanceof Damageable damageable)
			{
				Bukkit.broadcastMessage("damage: "
						+ (damageable.hasDamage() ? damageable.getDamage() : null)
						+ " maxdamage: " + (damageable.hasMaxDamage() ? damageable.getMaxDamage() : null));
				damageable.setDamage(damage);
				damageable.setMaxDamage(maxDamage);
			}
			
			item.setItemMeta(meta);
			
//			int i = Integer.parseInt(args[0]);
//
//			ItemStack item = new ItemStack(Material.LEATHER_HELMET);
//			ItemMeta meta = item.getItemMeta();
//			meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "", i, ADD_NUMBER, EquipmentSlot.HEAD));
//			item.setItemMeta(meta);
//			player.getInventory().addItem(item);
			
//			ItemStack goldenPickaxe = new ItemStack(Material.GOLDEN_PICKAXE);
//			ItemMeta meta = goldenPickaxe.getItemMeta();
//			ToolComponent tool = meta.getTool();
//
//			tool.addRule(Tag.MINEABLE_PICKAXE, 0.1F, true);
//			meta.setTool(tool);
//			goldenPickaxe.setItemMeta(meta);
//			player.getInventory().addItem(goldenPickaxe);
		}
		else if(args.length == 1)
		{
			player.teleport(new Location(Bukkit.getWorld("cidade_virtual"), -169.0D, 24.0d, -50.0d));
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
