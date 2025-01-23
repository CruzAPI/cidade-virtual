package com.eul4.listener.hotbar;

import com.eul4.Main;
import com.eul4.hotbar.RaidAnalyzerHotbar;
import com.eul4.model.player.spiritual.RaidAnalyzer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
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
		
		//TODO: As espadas dessa hotbar começou bugar por causa do toggle-combat,
		//      por isso é melhor verificar o item por NamespacedKeys e enums
		if(hotbar.getAttack() != null && item.getType() == hotbar.getAttack().getType())
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
