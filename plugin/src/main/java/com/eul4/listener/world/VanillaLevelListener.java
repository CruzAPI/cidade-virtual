package com.eul4.listener.world;

import com.eul4.Main;
import com.eul4.type.PluginWorldType;
import com.eul4.world.VanillaLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

@RequiredArgsConstructor
public class VanillaLevelListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(PlayerPortalEvent event)
	{
		Location from = event.getFrom();
		Location to = event.getTo();
		
		World worldFrom = from.getWorld();
		
		if(!(plugin.getWorldManager().get(worldFrom) instanceof VanillaLevel vanillaLevel))
		{
			return;
		}
		
		PluginWorldType related = vanillaLevel.getRelated(to.getWorld().getEnvironment());
		
		if(related == null)
		{
			return;
		}
		
		to.setWorld(related.getWorld());
	}
}
