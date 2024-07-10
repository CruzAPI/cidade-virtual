package com.eul4.common.listener.container;

import com.eul4.common.Common;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import static com.eul4.common.constant.CommonNamespacedKey.REMOVE_ON_CHUNK_LOAD;
import static org.bukkit.persistence.PersistentDataType.BOOLEAN;

@RequiredArgsConstructor
public class RemoveOnChunkLoadListener implements Listener
{
	private final Common plugin;
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void removeOnChunkLoad(ChunkLoadEvent event)
	{
		for(Entity entity : event.getChunk().getEntities())
		{
			final var container = entity.getPersistentDataContainer();
			
			boolean remove = container.getOrDefault(REMOVE_ON_CHUNK_LOAD, BOOLEAN, false);
			
			if(remove)
			{
				entity.remove();
			}
		}
	}
}
