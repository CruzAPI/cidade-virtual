package com.eul4.listener.player;

import com.eul4.Main;
import com.eul4.model.player.RaidAnalyzer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public class RaidAnalyzerListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof RaidAnalyzer raidAnalyzer))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void on(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof RaidAnalyzer raidAnalyzer))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void on(InventoryClickEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof RaidAnalyzer raidAnalyzer))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void on(InventoryDragEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof RaidAnalyzer raidAnalyzer))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void on(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof RaidAnalyzer raidAnalyzer))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void on(EntityPickupItemEvent event)
	{
		if(!(event.getEntity() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof RaidAnalyzer raidAnalyzer))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void on(PlayerDropItemEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof RaidAnalyzer raidAnalyzer))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void on(FoodLevelChangeEvent event)
	{
		if(!(event.getEntity() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof RaidAnalyzer raidAnalyzer))
		{
			return;
		}
		
		event.setCancelled(true);
	}
}
