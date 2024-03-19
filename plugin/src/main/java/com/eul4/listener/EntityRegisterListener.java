package com.eul4.listener;

//import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.HashMap;
import java.util.Map;

public class EntityRegisterListener implements Listener
{
	private final Map<Integer, Entity> entities = new HashMap<>();
	
//	@EventHandler
//	public void onEntityAdd(EntityAddToWorldEvent event)
//	{
//		Entity entity = event.getEntity();
//		entities.put(entity.getEntityId(), entity);
//	}
//
//	@EventHandler
//	public void onEntityRemove(EntityRemoveFromWorldEvent event)
//	{
//		entities.remove(event.getEntity().getEntityId());
//	}
	
	public Entity getEntityById(int id)
	{
		return entities.get(id);
	}
}
