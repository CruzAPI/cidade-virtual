package com.eul4.listener.player;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.eul4.Main;
import com.eul4.model.player.VanillaPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

@RequiredArgsConstructor
public class VanillaListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(PlayerPostRespawnEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof VanillaPlayer vanillaPlayer))
		{
			return;
		}
		
		if(player.getRespawnLocation() == null)
		{
			vanillaPlayer.performSpawnSkipChanneling();
		}
	}
}
