package com.eul4.listener;

import com.eul4.Common;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerManagerListener implements Listener
{
	private final Common plugin;
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		plugin.getPlayerManager().register(event.getPlayer(), plugin.getDefaultPlayerType());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		plugin.getPlayerManager().unregister(event.getPlayer());
	}
}
