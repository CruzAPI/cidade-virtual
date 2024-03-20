package com.eul4.listener;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.eul4.Main;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class EntityRegisterListener implements Listener
{
	private final Main plugin;
	
	private final Map<Integer, Entity> entities = new HashMap<>();
	
	@EventHandler
	public void onEntityAdd(EntityAddToWorldEvent event)
	{
		final Entity entity = event.getEntity();
		entities.put(entity.getEntityId(), entity);
	}

	@EventHandler
	public void onEntityRemove(EntityRemoveFromWorldEvent event)
	{
		plugin.getSpawnEntityInterceptor().unregister(event.getEntity().getEntityId());
		entities.remove(event.getEntity().getEntityId());
	}
	
	public Entity getEntityById(int id)
	{
		return entities.get(id);
	}
}
