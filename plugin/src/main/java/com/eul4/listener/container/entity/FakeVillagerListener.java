package com.eul4.listener.container.entity;

import com.eul4.Main;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static com.eul4.enums.PluginNamespacedKey.FAKE_VILLAGER;
import static org.bukkit.persistence.PersistentDataType.BOOLEAN;

@RequiredArgsConstructor
public class FakeVillagerListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractFakeVillager(PlayerInteractEntityEvent event)
	{
		Entity entity = event.getRightClicked();
		var container = entity.getPersistentDataContainer();
		
		boolean isFakeVillager = container.getOrDefault(FAKE_VILLAGER, BOOLEAN, false);
		
		if(isFakeVillager)
		{
			event.setCancelled(true);
		}
	}
}
