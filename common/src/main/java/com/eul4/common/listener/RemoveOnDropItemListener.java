package com.eul4.common.listener;

import com.eul4.common.Common;
import com.eul4.common.constant.CommonNamespacedKey;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

@RequiredArgsConstructor
public class RemoveOnDropItemListener implements Listener
{
	private final Common plugin;
	
	@EventHandler(ignoreCancelled = true)
	public void onItemSpawn(ItemSpawnEvent event)
	{
		Item item = event.getEntity();
		ItemStack itemStack = item.getItemStack();
		
		if(!itemStack.hasItemMeta())
		{
			return;
		}
		
		ItemMeta meta = itemStack.getItemMeta();
		var container = meta.getPersistentDataContainer();
		
		if(!container.has(CommonNamespacedKey.CANCEL_SPAWN, PersistentDataType.BOOLEAN))
		{
			return;
		}
		
		boolean cancelSpawn = container.get(CommonNamespacedKey.CANCEL_SPAWN, PersistentDataType.BOOLEAN);
		
		if(cancelSpawn)
		{
			event.setCancelled(true);
		}
	}
}
