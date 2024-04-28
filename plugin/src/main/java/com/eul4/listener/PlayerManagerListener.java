package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.TownPlayer;
import com.eul4.service.ExternalizablePlayerLoader;
import com.eul4.type.player.PluginPlayerType;
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
		
		ExternalizablePlayerLoader externalizablePlayerLoader = plugin.getExternalizablePlayerLoader();
		
		PluginPlayer memoryPluginPlayer = externalizablePlayerLoader.getUnsavedPlayers().get(player.getUniqueId());
		
		if(memoryPluginPlayer != null)
		{
			plugin.getPlayerManager().register(player, memoryPluginPlayer, memoryPluginPlayer.getLastLoadableCommonPlayerType().getPluginPlayerType());
			return;
		}
		
		PluginPlayer diskPluginPlayer = externalizablePlayerLoader.loadPlayer(player);
		
		if(diskPluginPlayer != null)
		{
			player.sendMessage("from disk: " + diskPluginPlayer.getCommonPlayerType().);
			plugin.getPlayerManager().register(player, diskPluginPlayer, diskPluginPlayer.getCommonPlayerType());
			return;
		}
		
		plugin.getPlayerManager().register(player, plugin, PluginPlayerType.TOWN_PLAYER);
		player.sendMessage("Welcome!");
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		plugin.getServer().getScheduler().runTask(plugin,
				() -> plugin.getPlayerManager().unregister(event.getPlayer()));
	}
}
