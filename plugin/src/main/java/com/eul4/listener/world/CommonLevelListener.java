package com.eul4.listener.world;

import com.eul4.Main;
import com.eul4.enums.Rarity;
import com.eul4.util.RarityUtil;
import com.eul4.world.NewbieLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Optional;

@RequiredArgsConstructor
public class CommonLevelListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onPlayerDeathKeepOnlyCommonItems(PlayerDeathEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getWorldManager().get(player.getWorld()) instanceof NewbieLevel newbieLevel))
		{
			return;
		}
		
		boolean hasKeepInventoryGameRule = Optional.ofNullable(newbieLevel.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY)).orElse(false);
		
		if(hasKeepInventoryGameRule)
		{
			return;
		}
		
		event.setKeepInventory(true);
		event.getDrops().clear();
		
		PlayerInventory playerInventory = player.getInventory();
		
		ItemStack[] contents = playerInventory.getContents();
		
		for(int i = 0; i < contents.length; i++)
		{
			ItemStack item = contents[i];
			
			if(item == null)
			{
				continue;
			}
			
			Rarity itemRarity = RarityUtil.getRarity(item);
			
			if(itemRarity == Rarity.COMMON)
			{
				continue;
			}
			
			event.getDrops().add(item);
			playerInventory.setItem(i, null);
		}
	}
}
