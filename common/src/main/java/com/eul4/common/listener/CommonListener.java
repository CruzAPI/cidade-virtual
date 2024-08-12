package com.eul4.common.listener;

import com.eul4.common.Common;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

@RequiredArgsConstructor
public class CommonListener implements Listener
{
	private final Common plugin;
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerLoginEvent event)
	{
		plugin.getOfflineUsernames().add(event.getPlayer().getName());
	}
}
