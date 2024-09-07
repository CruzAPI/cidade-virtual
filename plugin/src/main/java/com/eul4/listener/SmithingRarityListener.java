package com.eul4.listener;

import com.eul4.Main;
import com.eul4.enums.Rarity;
import com.eul4.util.RarityUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;

import static com.eul4.util.RarityUtil.getMinRarity;

@RequiredArgsConstructor
public class SmithingRarityListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(PrepareSmithingEvent event)
	{
		SmithingInventory smithingInventory = event.getInventory();
		
		if(!(smithingInventory.getHolder() instanceof BlockInventoryHolder blockInventoryHolder))
		{
			return;
		}
		
		Rarity minRarity = RarityUtil.getRarity(plugin, blockInventoryHolder.getBlock());
		
		minRarity = getMinRarity(minRarity, smithingInventory.getStorageContents());
		
		ItemStack result = event.getResult();
		
		if(result == null)
		{
			return;
		}
		
		RarityUtil.setRarity(result, minRarity);
		event.setResult(result);
	}
}
