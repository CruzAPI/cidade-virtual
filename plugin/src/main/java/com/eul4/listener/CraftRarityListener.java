package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.i18n.Messageable;
import com.eul4.enums.Rarity;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.util.RarityUtil;
import com.eul4.util.SoundUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import java.util.Optional;
import java.util.Set;

import static com.eul4.util.RarityUtil.getMinRarity;

@RequiredArgsConstructor
public class CraftRarityListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(CrafterCraftEvent event)
	{
		Rarity rarity = RarityUtil.getRarity(plugin, event.getBlock());
		Bukkit.broadcastMessage("RARIDADE:"+rarity);
		
		ItemStack result = event.getResult();
		RarityUtil.setRarity(result, rarity);
		event.setResult(result);
	}
	
//	Doesn't work
//	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(InventoryClickEvent event)
	{
		if(event.getInventory() instanceof CrafterInventory crafterInventory)
		{
			plugin.getServer().getScheduler().getMainThreadExecutor(plugin).execute(() -> update(crafterInventory));
		}
	}
	
//	Doesn't work
//	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(InventoryDragEvent event)
	{
		if(event.getInventory() instanceof CrafterInventory crafterInventory)
		{
			plugin.getServer().getScheduler().getMainThreadExecutor(plugin).execute(() -> update(crafterInventory));
		}
	}
	
//	Doesn't work, item lore change but lose lore next tick.
	private void update(CrafterInventory crafterInventory)
	{
		if(!(crafterInventory.getHolder() instanceof BlockInventoryHolder blockInventoryHolder))
		{
			return;
		}
		
		ItemStack result = crafterInventory.getItem(9);
		
		if(result == null || result.isEmpty())
		{
			return;
		}
		
		Rarity minRarity = RarityUtil.getRarity(plugin, blockInventoryHolder.getBlock());
		minRarity = getMinRarity(minRarity, crafterInventory.getStorageContents());
		RarityUtil.setRarity(result, minRarity);
		result.setType(Material.STONE);
		crafterInventory.setItem(9, result);
		crafterInventory.getViewers().forEach(x -> ((Player) x).updateInventory());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(PrepareItemCraftEvent event)
	{
		CraftingInventory craftingInventory = event.getInventory();
		
		Rarity minRarity = craftingInventory.getType() == InventoryType.WORKBENCH
			? RarityUtil.getRarity(plugin, craftingInventory.getLocation().getBlock())
			: Rarity.MAX_RARITY;
		
		minRarity = getMinRarity(minRarity, craftingInventory.getMatrix());
		
		ItemStack result = craftingInventory.getResult();
		RarityUtil.setRarity(result, minRarity);
		craftingInventory.setResult(result);
	}
}
