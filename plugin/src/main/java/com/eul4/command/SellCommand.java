package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.RaidPerformer;
import com.eul4.service.MarketDataManager;
import com.eul4.wrapper.CryptoInfo;
import com.eul4.wrapper.MaterialMultiplier;
import com.eul4.wrapper.Trade;
import lombok.RequiredArgsConstructor;
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
import java.util.Set;

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
			int amount = item.getAmount();
			
			if(material.isEmpty())
			{
				pluginPlayer.sendMessage(PluginMessage.COMMAND_SELL_NEED_HOLD_ITEM);
				return false;
			}
			
			Set<Trade> trades = marketDataManager.getTrades(item);
			
			if(trades.isEmpty())
			{
				pluginPlayer.sendMessage(PluginMessage.COMMAND_SELL_ITEM_NOT_FOR_SALE);
				return false;
			}
			
			BigDecimal tradeResult = BigDecimal.ZERO;
			
			for(Trade trade : trades)
			{
				tradeResult = tradeResult.add(trade.execute());
			}
			
			player.getInventory().setItemInMainHand(null);
			
			pluginPlayer.sendMessage
			(
				PluginMessage.COMMAND_SELL_SOLD_$MATERIAL_$AMOUNT_$VALUE,
				Component.translatable(material.translationKey()),
				item.getAmount(),
				tradeResult
			);
			return true;
		}
		else
		{
//			pluginPlayer.sendMessage(PluginMessage.COMMAND_RAID_USE_$ALIASES, aliases);
			return false;
		}
	}
}
