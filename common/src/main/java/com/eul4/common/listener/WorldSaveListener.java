package com.eul4.common.listener;

import com.eul4.common.Common;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

@RequiredArgsConstructor
public class WorldSaveListener implements Listener
{
	private final Common plugin;
	
	@EventHandler
	public void onWorldSave(WorldSaveEvent event)
	{
		if(event.getWorld() != plugin.getMainWorldType().getWorld())
		{
			return;
		}
		
		plugin.getServerTickCounter().saveOrLogError();
	}
}
