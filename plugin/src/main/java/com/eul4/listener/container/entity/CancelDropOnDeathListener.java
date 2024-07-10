package com.eul4.listener.container.entity;

import com.eul4.Main;
import com.eul4.common.constant.CommonNamespacedKey;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static com.eul4.enums.PluginNamespacedKey.FAKE_VILLAGER;
import static org.bukkit.persistence.PersistentDataType.BOOLEAN;

@RequiredArgsConstructor
public class CancelDropOnDeathListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void cancelDropOnDeath(EntityDeathEvent event)
	{
		Entity entity = event.getEntity();
		var container = entity.getPersistentDataContainer();
		
		boolean cancelDrop = container.getOrDefault(CommonNamespacedKey.CANCEL_DROP, BOOLEAN, false);
		
		if(cancelDrop)
		{
			event.getDrops().clear();
			event.setDroppedExp(0);
		}
	}
}
