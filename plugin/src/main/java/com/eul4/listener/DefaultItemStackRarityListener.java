package com.eul4.listener;

import com.eul4.Main;
import com.eul4.util.RarityUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class DefaultItemStackRarityListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityPickup(EntityPickupItemEvent event)
	{
		RarityUtil.setRarityIfAbsent(event.getItem().getItemStack());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryPickup(InventoryPickupItemEvent event)
	{
		RarityUtil.setRarityIfAbsent(event.getItem().getItemStack());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		setDefaultRarityToInventoryContentsIfAbsent(event.getPlayer().getInventory());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryOpen(InventoryOpenEvent event)
	{
		setDefaultRarityToInventoryContentsIfAbsent(event.getInventory());
	}
	
	private void setDefaultRarityToInventoryContentsIfAbsent(Inventory inventory)
	{
		ItemStack[] contents = inventory.getContents();
		
		for(int i = 0; i < contents.length; i++)
		{
			ItemStack content = contents[i];
			
			if(content == null)
			{
				continue;
			}
			
			inventory.setItem(i, RarityUtil.setRarityIfAbsent(content));
		}
	}
}
