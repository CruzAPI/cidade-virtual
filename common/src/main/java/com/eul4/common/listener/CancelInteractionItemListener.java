package com.eul4.common.listener;

import com.eul4.common.Common;
import com.eul4.common.constant.CommonNamespacedKey;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

@RequiredArgsConstructor
public class CancelInteractionItemListener implements Listener
{
	private final Common plugin;
	
	@EventHandler(ignoreCancelled = true)
	public void onInteraction(PlayerInteractEvent event)
	{
		ItemStack item = event.getItem();
		
		if(item == null || !item.hasItemMeta())
		{
			return;
		}
		
		ItemMeta meta = item.getItemMeta();
		var container = meta.getPersistentDataContainer();
		
		if(!container.has(CommonNamespacedKey.CANCEL_INTERACTION, PersistentDataType.BOOLEAN))
		{
			return;
		}
		
		boolean cancelInteraction = container.get(CommonNamespacedKey.CANCEL_INTERACTION, PersistentDataType.BOOLEAN);
		
		if(cancelInteraction)
		{
			event.setCancelled(true);
		}
	}
}
