package com.eul4.service;

import com.eul4.Price;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.town.Town;
import com.eul4.wrapper.Cost;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.BooleanSupplier;

@RequiredArgsConstructor
@Getter
public class PurchaseV2
{
	private final PluginPlayer pluginPlayer;
	private final Cost cost;
	private final BooleanSupplier execution;
	
	public PurchaseV2(PluginPlayer pluginPlayer, Price price, BooleanSupplier execution)
	{
		this.pluginPlayer = pluginPlayer;
		this.cost = new Cost(price);
		this.execution = execution;
	}
	
	public boolean isAffordable()
	{
		return isAffordable(false);
	}
	
	public boolean isAffordable(boolean sendMessage)
	{
		return isPriceAffordable(sendMessage) & isResourcesAffordable(sendMessage);
	}
	
	private boolean isResourcesAffordable(boolean sendMessage)
	{
		boolean affordable = true;
		
		for(Map.Entry<Material, Integer> entry : cost.getResources().entrySet())
		{
			Material type = entry.getKey();
			int requiredAmount = entry.getValue();
			int currentAmount = countAmount(pluginPlayer.getPlayer().getInventory(), type);
			int missingAmount = requiredAmount - currentAmount;
			
			if(missingAmount > 0)
			{
				affordable = false;
				pluginPlayer.sendMessage(sendMessage, PluginMessage.MISSING_RESOURCES, missingAmount, type);
			}
		}
		
		return affordable;
	}
	
	private int countAmount(Inventory inventory, Material type)
	{
		int amountCounter = 0;
		
		for(ItemStack content : inventory.getContents())
		{
			if(content == null || content.getType() != type)
			{
				continue;
			}
			
			amountCounter += content.getAmount();
		}
		
		return amountCounter;
	}
	
	private boolean isPriceAffordable(final boolean sendMessage)
	{
		Price price = cost.getPrice();
		
		int currentLikes = pluginPlayer.findTown().map(Town::getLikes).orElse(0);
		int currentDislikes = pluginPlayer.findTown().map(Town::getDislikes).orElse(0);
		int missingLikes = price.getLikes() - currentLikes;
		int missingDislikes = price.getDislikes() - currentDislikes;
		
		boolean affordable = true;
		
		if(missingLikes > 0)
		{
			affordable = false;
			pluginPlayer.sendMessage(sendMessage, PluginMessage.MISSING_LIKES, missingLikes);
		}
		
		if(missingDislikes > 0)
		{
			affordable = false;
			pluginPlayer.sendMessage(sendMessage, PluginMessage.MISSING_DISLIKES, missingDislikes);
		}
		
		return affordable;
	}
	
	public boolean isPriceValid(Price price, boolean sendMessage)
	{
		boolean valid = price.equals(this.cost.getPrice());
		
		if(sendMessage && !valid)
		{
			pluginPlayer.sendMessage(PluginMessage.PURCHASE_INVALID_PRICE);
		}
		
		return valid;
	}
	
	public boolean tryExecutePurchaseValidatingPrice(Price price)
	{
		if(!isPriceValid(price, true))
		{
			return false;
		}
		
		return tryExecutePurchase();
	}
	
	public boolean tryExecutePurchase()
	{
		if(!isAffordable(true))
		{
			return false;
		}
		
		boolean wasExecuted = execution.getAsBoolean();
		
		if(!wasExecuted)
		{
			return false;
		}
		
		debit();
		return true;
	}
	
	public void debit()
	{
		debitPrice();
		debitResources();
		sendDebitMessage();
	}
	
	private void debitPrice()
	{
		pluginPlayer.findTown().ifPresent(town -> town.subtract(cost.getPrice()));
	}
	
	
	private void debitResources()
	{
		for(Map.Entry<Material, Integer> entry : cost.getResources().entrySet())
		{
			debitResource(entry.getKey(), entry.getValue());
		}
	}
	
	private void sendDebitMessage()
	{
		Player player = pluginPlayer.getPlayer();
		
		Price price = cost.getPrice();
		
		//TODO color and translate messages
		
		if(price.getLikes() > 0)
		{
			player.sendMessage(Component.text("-")
					.append(Component.text(price.getLikes() + " "))
					.append(Component.text("LIKES")));
		}
		
		if(price.getDislikes() > 0)
		{
			player.sendMessage(Component.text("-")
					.append(Component.text(price.getDislikes() + " "))
					.append(Component.text("DISLIKES")));
		}
		
		for(Map.Entry<Material, Integer> entry : cost.getResources().entrySet())
		{
			player.sendMessage(Component.text("-")
					.append(Component.text(entry.getValue() + " "))
					.append(Component.translatable(entry.getKey().translationKey())));
		}
	}
	
	private void debitResource(Material type, int amount)
	{
		Inventory inventory = pluginPlayer.getPlayer().getInventory();
		ItemStack[] contents = inventory.getContents();
		
		for(int slot = 0; slot < contents.length && amount > 0; slot++)
		{
			ItemStack content = contents[slot];
			
			if(content == null || content.getType() != type)
			{
				continue;
			}
			
			if(content.getAmount() > amount)
			{
				content.setAmount(content.getAmount() - amount);
				amount = 0;
			}
			else
			{
				amount -= content.getAmount();
				content = null;
			}
			
			inventory.setItem(slot, content);
		}
	}
}
