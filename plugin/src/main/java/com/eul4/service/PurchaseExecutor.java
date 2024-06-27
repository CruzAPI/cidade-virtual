package com.eul4.service;

import com.eul4.Main;
import com.eul4.Price;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.util.concurrent.locks.Condition;

@RequiredArgsConstructor
public class PurchaseExecutor
{
	private final Main plugin;
	
	public boolean executePurchase(Purchase purchase)
	{
		CommonPlayer commonPlayer = purchase.getCommonPlayer();
		Town town = purchase.getTown();
		Price price = purchase.getPrice();
		
		int missingLikes = price.getLikes() - town.getLikes();
		int missingDislikes = price.getDislikes() - town.getDislikes();
		
		if(missingLikes > 0 || missingDislikes > 0)
		{
			messageMissingBalance(commonPlayer, missingLikes, missingDislikes);
			return false;
		}
		
		if(!purchase.getExecution().execute())
		{
			return false;
		}
		
		town.subtract(price);
		messagePriceSubtracted(commonPlayer, price);
		return true;
	}
	
	private void messagePriceSubtracted(CommonPlayer commonPlayer, Price price)
	{
		if(price.getLikes() > 0)
		{
			commonPlayer.getPlayer().sendMessage("-" + price.getLikes() + " LIKES");
		}
		
		if(price.getDislikes() > 0)
		{
			commonPlayer.getPlayer().sendMessage("-" + price.getDislikes() + " DISLIKES");
		}
	}
	
	private void messageMissingBalance(CommonPlayer commonPlayer, int missingLikes, int missingDislikes)
	{
		if(missingLikes > 0)
		{
			commonPlayer.sendMessage(PluginMessage.MISSING_LIKES, missingLikes);
		}
		
		if(missingDislikes > 0)
		{
			commonPlayer.sendMessage(PluginMessage.MISSING_DISLIKES, missingDislikes);
		}
	}
}
