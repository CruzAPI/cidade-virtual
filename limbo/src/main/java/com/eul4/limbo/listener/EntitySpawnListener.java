package com.eul4.limbo.listener;

import com.eul4.limbo.Main;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

@RequiredArgsConstructor
public class EntitySpawnListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void cancelEntitySpawn(EntitySpawnEvent event)
	{
		event.setCancelled(true);
	}
}
