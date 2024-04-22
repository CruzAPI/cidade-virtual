package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.player.TownPlayer;
import com.eul4.type.player.PluginPlayerType;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerManagerListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		plugin.getPlayerManager().register(event.getPlayer(), plugin, PluginPlayerType.TOWN_PLAYER);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		plugin.getServer().getScheduler().runTask(plugin,
				() -> plugin.getPlayerManager().unregister(event.getPlayer()));
	}
}
