package com.eul4.common.listener;

import com.eul4.common.Common;
import com.eul4.common.event.CommonPlayerRegisterEvent;
import com.eul4.common.model.player.CommonPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class CommonPlayerListener implements Listener
{
	private final Common plugin;
	
	@EventHandler
	public void on(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		commonPlayer.savePlayerData();
	}
	
	@EventHandler
	public void on(CommonPlayerRegisterEvent event)
	{
		CommonPlayer oldCommonPlayer = event.getOldCommonPlayer();
		
		if(oldCommonPlayer != null)
		{
			oldCommonPlayer.savePlayerData();
		}
	}
}
