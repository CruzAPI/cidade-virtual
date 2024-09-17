package com.eul4.listener;

import com.eul4.Main;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

@RequiredArgsConstructor
public class PluginFilerListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(WorldSaveEvent event)
	{
		if(event.getWorld() == plugin.getMainWorldType().getWorld())
		{
			plugin.getRawMaterialMapFiler().save();
			plugin.getCrownInfoFiler().save();
		}
	}
}
