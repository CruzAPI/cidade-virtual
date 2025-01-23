package com.eul4.listener.hotbar;

import com.eul4.Main;
import com.eul4.hotbar.RaidSpectatorHotbar;
import com.eul4.model.player.spiritual.RaidSpectator;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class RaidSpectatorHotbarListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		
		if(item == null || !(plugin.getPlayerManager().get(player) instanceof RaidSpectator raidSpectator))
		{
			return;
		}
		
		RaidSpectatorHotbar hotbar = raidSpectator.getHotbar();
		
		if(item.equals(hotbar.getDefend()))
		{
			raidSpectator.defend();
		}
		else if(item.equals(hotbar.getVanilla()))
		{
			raidSpectator.vanilla();
		}
	}
}
