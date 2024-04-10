package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.event.WorldSaveOrStopEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

@RequiredArgsConstructor
public class BlockDataSaveListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onWorldSave(WorldSaveOrStopEvent event)
	{
		plugin.getBlockDataLoader().saveChunks(event.getWorld());
	}
}
