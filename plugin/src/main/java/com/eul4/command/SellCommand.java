package com.eul4.command;

import com.eul4.Main;
import com.eul4.economy.ItemStackTransaction;
import com.eul4.economy.Transaction;
import com.eul4.exception.InvalidCryptoInfoException;
import com.eul4.exception.MaterialNotForSaleException;
import com.eul4.exception.OperationException;
import com.eul4.exception.OverCapacityException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.service.MarketDataManager;
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

public class SellCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "sell";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME, "vender" };
	
	private final Main plugin;
	
	private final MarketDataManager marketDataManager;
	
	public SellCommand(Main plugin)
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
			int slot = player.getInventory().getHeldItemSlot();
			
			if(material.isEmpty())
			{
				pluginPlayer.sendMessage(PluginMessage.COMMAND_SELL_NEED_HOLD_ITEM);
				return false;
			}
			
			try
			{
				ItemStackTransaction itemStackTransaction = marketDataManager
						.createItemStackTransaction(pluginPlayer, slot);
				itemStackTransaction.execute();
				BigDecimal total = itemStackTransaction.getTransaction().getTotal();
				
				pluginPlayer.sendMessage
				(
					PluginMessage.COMMAND_SELL_SOLD_$MATERIAL_$AMOUNT_$VALUE,
					Component.translatable(material.translationKey()),
					itemStackTransaction.getAmountToConsume(),
					total
				);
				return true;
			}
			catch(MaterialNotForSaleException | InvalidCryptoInfoException e)
			{
				pluginPlayer.sendMessage(PluginMessage.COMMAND_SELL_ITEM_NOT_FOR_SALE);
				return false;
			}
			catch(OverCapacityException e)
			{
				pluginPlayer.sendMessage(PluginMessage.CROWN_DEPOSITS_INSUFFICIENT_CAPACITY);
				return false;
			}
			catch(OperationException e)
			{
				pluginPlayer.sendMessage(PluginMessage.EXCEPTION_OPERATION);
				return false;
			}
		}
		else
		{
			pluginPlayer.sendMessage(PluginMessage.COMMAND_SELL_USE_$ALIASES, aliases);
			return false;
		}
	}
}
