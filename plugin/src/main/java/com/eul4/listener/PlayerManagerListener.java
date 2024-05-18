package com.eul4.listener;

import com.eul4.Main;
import com.eul4.externalizer.filer.PlayerDataFiler;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.PhysicalPlayerType;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
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
		Player player = event.getPlayer();
		
		PlayerDataFiler playerDataFiler = plugin.getPlayerDataFiler();
		
		PluginPlayer pluginPlayer = playerDataFiler.loadFromMemoryOrDiskLogging(player);
		
		if(pluginPlayer != null)
		{
			pluginPlayer = (PluginPlayer) plugin.getPlayerManager().register(player, pluginPlayer);
			pluginPlayer.load();
			return;
		}
		
		plugin.getPlayerManager().register(player, plugin, PhysicalPlayerType.VANILLA_PLAYER);
		player.sendMessage("Welcome!");
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		plugin.getServer().getScheduler().runTask(plugin,
				() -> plugin.getPlayerManager().unregister(event.getPlayer()));
	}
}
