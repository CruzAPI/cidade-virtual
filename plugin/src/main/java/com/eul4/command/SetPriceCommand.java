package com.eul4.command;

import com.eul4.Main;
import com.eul4.Price;
import com.eul4.StructurePriceChart;
import com.eul4.StructureType;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.Currency;
import com.eul4.i18n.PluginMessage;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class SetPriceCommand implements TabExecutor
{
	private final Main plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return true;
		}
		
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(!player.isOp())
		{
			commonPlayer.sendMessage(CommonMessage.YOU_DO_NOT_HAVE_PERMISSION);
			return true;
		}
		
		if(args.length == 4)
		{
			final StructureType structureType;
			final int level;
			final Currency currency;
			final double value;
			
			try
			{
				structureType = StructureType.valueOf(args[0].toUpperCase());
			}
			catch(IllegalArgumentException e)
			{
				commonPlayer.sendMessage(PluginMessage.STRUCTURE_TYPE_NOT_FOUND);
				return true;
			}
			
			try
			{
				level = Integer.parseInt(args[1]);
			}
			catch(NumberFormatException e)
			{
				commonPlayer.sendMessage(PluginMessage.LEVEL_MUST_BE_AN_INT);
				return true;
			}
			
			try
			{
				currency = Currency.valueOf(args[2].toUpperCase());
			}
			catch(IllegalArgumentException e)
			{
				commonPlayer.sendMessage(PluginMessage.CURRENCY_NOT_FOUND);
				return true;
			}
			
			try
			{
				value = Double.parseDouble(args[3]);
			}
			catch(NumberFormatException e)
			{
				commonPlayer.sendMessage(PluginMessage.VALUE_MUST_BE_A_DOUBLE);
				return true;
			}
			
			StructurePriceChart structurePriceChart = plugin.getStructurePriceChart();
			
			Price price = structurePriceChart.createPriceIfNotExists(structureType, level);
			
			if(currency == Currency.LIKE)
			{
				price.setLikes((int) value);
			}
			else if(currency == Currency.DISLIKE)
			{
				price.setDislikes((int) value);
			}
			else
			{
				commonPlayer.sendMessage(PluginMessage.CURRENCY_NOT_FOUND);
				return true;
			}
			
			commonPlayer.sendMessage(PluginMessage.COMMAND_SETPRICE_VALUE_SET, structureType, level, value, currency);
			
			try
			{
				plugin.getStructurePriceSerializer().saveStructurePriceChart(structurePriceChart);
			}
			catch(IOException e)
			{
				e.printStackTrace();
				commonPlayer.sendMessage(PluginMessage.FAILED_TO_SAVE_STRUCTURE_PRICES_FILE);
			}
		}
		else
		{
			commonPlayer.sendMessage(PluginMessage.COMMAND_SETPRICE_USAGE, alias);
		}
		
		return false;
	}
}
