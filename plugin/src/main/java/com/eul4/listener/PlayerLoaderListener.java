package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.event.CommonPlayerRegisterEvent;
import com.eul4.model.player.PluginPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

@RequiredArgsConstructor
public class PlayerLoaderListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(CommonPlayerRegisterEvent event)
	{
		if(event.getCommonPlayer() instanceof PluginPlayer pluginPlayer)
		{
			plugin.getPlayerDataFiler().getMemoryPlayers()
					.put(pluginPlayer.getUniqueId(), pluginPlayer);
		}
	}
	
	@EventHandler
	public void on(WorldSaveEvent event)
	{
		if(event.getWorld() == plugin.getMainWorldType().getWorld())
		{
			plugin.getPlayerDataFiler().saveMemoryPlayers();
		}
	}
}
