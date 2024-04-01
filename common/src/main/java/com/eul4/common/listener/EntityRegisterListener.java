package com.eul4.common.listener;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.eul4.common.Common;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class EntityRegisterListener implements Listener
{
	private final Common plugin;
	
	@Getter
	private final Map<UUID, Entity> persistentEntities = new HashMap<>();
	
	@EventHandler
	public void onEntityAdd(EntityAddToWorldEvent event)
	{
		final Entity entity = event.getEntity();
		
		if(entity.isPersistent())
		{
			persistentEntities.put(entity.getUniqueId(), entity);
		}
	}

	@EventHandler
	public void onEntityRemove(EntityRemoveFromWorldEvent event)
	{
		plugin.getSpawnEntityInterceptor().unregister(event.getEntity().getEntityId());
		persistentEntities.remove(event.getEntity().getUniqueId());
	}
	
	public void loadEntities()
	{
		persistentEntities.clear();
		
		for(World world : plugin.getServer().getWorlds())
		{
			plugin.getServer().getLogger().warning(world.getName() + ": " + world.getEntities().size());
			for(Entity entity : world.getEntities())
			{
				if(entity.isPersistent())
				{
					persistentEntities.put(entity.getUniqueId(), entity);
				}
			}
		}
		
		plugin.getServer().getLogger().warning("persistentEntities: (" + persistentEntities.size() + ") "
				+ persistentEntities);
	}
	
	public Entity getEntityByUuid(UUID uuid)
	{
		return persistentEntities.get(uuid);
	}
}
