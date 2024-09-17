package com.eul4.command;

import com.eul4.Main;
import com.eul4.exception.InvalidCryptoInfoException;
import com.eul4.exception.MaterialNotForSaleException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.service.MarketDataManager;
import com.eul4.wrapper.CryptoInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class PriceCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "price";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME, "preço" };
	
	private final Main plugin;
	
	private final MarketDataManager marketDataManager;
	
	public PriceCommand(Main plugin)
	{
		this.plugin = plugin;
		this.marketDataManager = plugin.getMarketDataManager();
	}
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String aliases, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return true;
		}
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(args.length == 0)
		{
			ItemStack item = player.getInventory().getItemInMainHand();
			Material material = item.getType();
			
			if(material.isEmpty())
			{
				pluginPlayer.sendMessage(PluginMessage.COMMAND_SELL_NEED_HOLD_ITEM);
				return false;
			}
			
			try
			{
				BigDecimal price = marketDataManager.calculatePrice(material);
				pluginPlayer.sendMessage(PluginMessage.COMMAND_PRICE_UNIT_PRICE_$MATERIAL_$PRICE,
						Component.translatable(material.translationKey()),
						price);
				return true;
			}
			catch(MaterialNotForSaleException | InvalidCryptoInfoException e)
			{
				pluginPlayer.sendMessage(PluginMessage.COMMAND_SELL_ITEM_NOT_FOR_SALE);
				return false;
			}
		}
		else
		{
			pluginPlayer.sendMessage(PluginMessage.COMMAND_PRICE_USE_$ALIASES, aliases);
			return false;
		}
	}
}
