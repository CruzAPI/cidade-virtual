package com.eul4.common.listener;

import com.eul4.common.Common;
import com.eul4.common.event.CommonPlayerRegisterEvent;
import com.eul4.common.model.player.CommonPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Illager;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldSaveEvent;

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
		CommonPlayer oldCommonPlayer = event.getCommonPlayer().getOldInstance();
		
		if(oldCommonPlayer != null)
		{
			oldCommonPlayer.savePlayerData();
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(WorldSaveEvent event)
	{
		if(event.getWorld() == plugin.getMainWorldType().getInstance().getWorld())
		{
			for(CommonPlayer commonPlayer : plugin.getPlayerManager().getAll())
			{
				commonPlayer.savePlayerData();
			}
		}
	}
}
