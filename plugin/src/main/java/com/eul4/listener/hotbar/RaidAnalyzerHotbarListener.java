package com.eul4.listener.hotbar;

import com.eul4.Main;
import com.eul4.hotbar.RaidAnalyzerHotbar;
import com.eul4.model.player.RaidAnalyzer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class RaidAnalyzerHotbarListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		
		if(item == null || !(plugin.getPlayerManager().get(player) instanceof RaidAnalyzer raidAnalyzer))
		{
			return;
		}
		
		RaidAnalyzerHotbar hotbar = raidAnalyzer.getHotbar();
		
		if(item.equals(hotbar.getAttack()))
		{
			raidAnalyzer.attack();
		}
		else if(item.equals(hotbar.getReroll()))
		{
			raidAnalyzer.reroll();
		}
		else if(item.equals(hotbar.getCancel()))
		{
			raidAnalyzer.cancel();
		}
	}
}
