package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.Rarity;
import com.eul4.i18n.PluginMessage;
import com.eul4.util.RarityUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class SetRarityCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "set-rarity";
	private static final String PERMISSION = "command." + COMMAND_NAME;
	
	private final Main plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(!(commandSender instanceof Player))
		{
			return Collections.emptyList();
		}
		
		if(args.length == 1)
		{
			List<String> suggestions = new ArrayList<>();
			
			for(Rarity rarity : Rarity.values())
			{
				if(rarity.name().toLowerCase().startsWith(args[0].toLowerCase()))
				{
					suggestions.add(rarity.name());
				}
			}
			
			return suggestions;
		}
		
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String aliases, String[] args)
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
		
		if(args.length == 1)
		{
			try
			{
				Rarity rarity = Rarity.valueOf(args[0].toUpperCase());
				ItemStack item = player.getInventory().getItemInMainHand();
				RarityUtil.setRarity(item, rarity);
				player.getInventory().setItemInMainHand(item);
				commonPlayer.sendMessage(PluginMessage.COMMAND_SET_RARITY_RARITY_SET, rarity);
				return true;
			}
			catch(IllegalArgumentException e)
			{
				commonPlayer.sendMessage(PluginMessage.COMMAND_SET_RARITY_RARITY_NOT_FOUND);
				return false;
			}
		}
		else
		{
			commonPlayer.sendMessage(PluginMessage.COMMAND_SET_RARITY_USE_$ALIASES, aliases);
			return false;
		}
	}
}
