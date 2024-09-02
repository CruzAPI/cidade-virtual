package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.PlayerType;
import com.eul4.common.util.ItemStackUtil;
import com.eul4.enums.Rarity;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.Admin;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.util.RarityUtil;
import com.eul4.world.OverWorld;
import com.eul4.wrapper.EnchantType;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

@RequiredArgsConstructor
public class EnchantCommand implements TabExecutor
{
	private static final String COMMAND_NAME = "enchant";
	private static final String PERMISSION = "command." + COMMAND_NAME;
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME };
	
	private final Main plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(args.length == 1)
		{
			List<String> suggestions = new ArrayList<>();
			
			for(EnchantType enchantType : EnchantType.values())
			{
				if(enchantType.name().startsWith(args[0].toUpperCase()))
				{
					suggestions.add(enchantType.name());
				}
			}
			
			return suggestions;
		}
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return false;
		}
		
		final CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(!commonPlayer.hasPermission(PERMISSION))
		{
			commonPlayer.sendMessage(CommonMessage.YOU_DO_NOT_HAVE_PERMISSION);
			return false;
		}
		
		if(args.length == 1 && args[0].equalsIgnoreCase("clear"))
		{
			ItemStack item = player.getInventory().getItemInMainHand();
			ItemMeta meta = item.getItemMeta();
			
			if(meta == null)
			{
				player.sendMessage("Item meta is null");
				return false;
			}
			
			meta.removeEnchantments();
			
			if(meta instanceof EnchantmentStorageMeta enchantmentStorageMeta)
			{
				enchantmentStorageMeta
						.getStoredEnchants()
						.keySet()
						.forEach(enchantmentStorageMeta::removeStoredEnchant);
			}
			
			item.setItemMeta(meta);
			player.getInventory().setItemInMainHand(item);
			player.sendMessage("Enchants cleared!");
		}
		else if(args.length == 2)
		{
			try
			{
				EnchantType enchantType = EnchantType.valueOf(args[0]);
				int level = Integer.parseInt(args[1]);
				
				ItemStack item = player.getInventory().getItemInMainHand();
				ItemMeta meta = item.getItemMeta();
				
				Rarity itemRarity = RarityUtil.getRarity(item);
				
				if(meta == null)
				{
					player.sendMessage("Item meta is null");
					return false;
				}
				
				if(!enchantType.getEnchantment().canEnchantItem(item) && item.getType() != Material.ENCHANTED_BOOK)
				{
					player.sendMessage(enchantType.name() + " can't enchant that item.");
					return false;
				}
				
				if(level > enchantType.getMaxLevel(itemRarity))
				{
					player.sendMessage(enchantType.name() + " max level is " + enchantType.getMaxLevel(itemRarity) + ".");
					return true;
				}
				
				for(Enchantment enchantment : ItemStackUtil.getEnchantments(item).keySet())
				{
					if(enchantment.conflictsWith(enchantType.getEnchantment()))
					{
						player.sendMessage(enchantType.name() + " is conflicting with " + enchantment.key().value().toUpperCase() + ".");
						return false;
					}
				}
				
				ItemStackUtil.addEnchant(item, enchantType.getEnchantment(), level, true);
				player.setItemInHand(item);
				player.sendMessage("Enchant added!");
			}
			catch(NumberFormatException e)
			{
				player.sendMessage("Invalid level!");
			}
			catch(IllegalArgumentException e)
			{
				player.sendMessage("Enchant not found!");
			}
		}
		else
		{
			player.sendMessage("Try:");
			player.sendMessage("/enchant <ench> <level>");
			player.sendMessage("/enchant clear");
		}
		
		return true;
	}
}
