package com.eul4.model.player;

import com.eul4.Price;
import com.eul4.common.world.CommonWorld;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.craft.CraftConfirmationGui;
import com.eul4.service.PurchaseV2;
import com.eul4.util.MessageUtil;
import com.eul4.world.HomeableLevel;
import com.eul4.wrapper.Cost;
import com.eul4.wrapper.HomeMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface SetHomePerformer extends PluginPlayer
{
	Cost COST = new Cost(new Price(10000, 0));
	int MAX_HOME = 3;
	int HOME_NAME_MAX_LENGTH = 16;
	String RESPAWN_HOME = "respawn";
	Set<String> HOME_KEY_WORDS = Set.of(RESPAWN_HOME);
	
	default boolean performSetHome(String[] args)
	{
		if(args.length == 1)
		{
			final Player player = getPlayer();
			final String homeName = args[0];
			
			if(!validateIfCanSetHome(homeName, player.getLocation()))
			{
				return false;
			}
			
			PurchaseV2 purchase = new PurchaseV2(this, COST, () -> setHome(homeName));
			
			if(!purchase.isAffordable(true))
			{
				return false;
			}
			
			openGui(new CraftConfirmationGui(this, purchase::tryExecutePurchase)
			{
				@Override
				public ItemStack getConfirm()
				{
					ItemStack item = super.getConfirm();
					ItemMeta meta = item.getItemMeta();;
					
					List<Component> lore = new ArrayList<>();
					
					lore.addAll(MessageUtil.getCostLoreV2(purchase.getCost(), SetHomePerformer.this));
					lore.add(Component.empty());
					lore.addAll(PluginMessage.COMMAND_SETHOME_CONFIRMATION_LORE.translateLines(SetHomePerformer.this, purchase.getCost()));
					
					meta.lore(lore);
					
					item.setItemMeta(meta);
					
					return item;
				}
			});
			return true;
		}
		else
		{
			sendMessage(PluginMessage.COMMAND_SETHOME_USAGE);
			return false;
		}
	}
	
	private boolean setHome(String homeName)
	{
		Location location = getPlayer().getLocation();
		
		if(!validateIfCanSetHome(homeName, location))
		{
			return false;
		}
		
		HomeMap homeMap = getVanillaPlayerData().getHomeMap();
		homeMap.put(homeName, location);
		sendMessage(PluginMessage.COMMAND_SETHOME_HOME_SET, homeName);
		return true;
	}
	
	private boolean validateIfCanSetHome(String homeName, Location location)
	{
		CommonWorld commonWorld = getPlugin().getWorldManager().get(location.getWorld());
		HomeMap homeMap = getVanillaPlayerData().getHomeMap();
		
		if(homeMap.size() >= MAX_HOME)
		{
			sendMessage(PluginMessage.COMMAND_SETHOME_MAX_HOME_REACHED);
			return false;
		}
		
		if(!(commonWorld instanceof HomeableLevel homeableLevel))
		{
			sendMessage(PluginMessage.COMMAND_SETHOME_NEED_TO_BE_IN_VANILLA);
			return false;
		}
		
		if(homeableLevel.isNearSpawn(location))
		{
			sendMessage(PluginMessage.COMMAND_SETHOME_NEED_TO_BE_AWAY_$RADIUS, homeableLevel.getNearSpawnRadius());
			return false;
		}
		
		if(homeName.isBlank())
		{
			sendMessage(PluginMessage.COMMAND_SETHOME_HOME_NAME_MUST_NOT_BE_BLANK);
			return false;
		}
		
		if(homeName.length() > HOME_NAME_MAX_LENGTH)
		{
			sendMessage(PluginMessage.COMMAND_SETHOME_HOME_NAME_MAX_LENGTH);
			return false;
		}
		
		if(homeName.matches(".*[^\\p{Digit}\\p{Alpha}].*"))
		{
			sendMessage(PluginMessage.COMMAND_SETHOME_HOME_NAME_LETTERS_AND_NUMBERS);
			return false;
		}
		
		if(HOME_KEY_WORDS.contains(homeName))
		{
			sendMessage(PluginMessage.COMMAND_SETHOME_HOME_NAME_KEY_WORD, homeName);
			return false;
		}
		
		if(homeMap.containsKey(homeName))
		{
			sendMessage(PluginMessage.COMMAND_SETHOME_HOME_ALREADY_SET, homeName);
			return false;
		}
		
		return true;
	}
}
