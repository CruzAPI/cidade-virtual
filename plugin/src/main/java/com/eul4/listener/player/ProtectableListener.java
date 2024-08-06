package com.eul4.listener.player;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import com.eul4.Main;
import com.eul4.model.player.Protectable;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

@RequiredArgsConstructor
public class ProtectableListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof Protectable))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof Protectable protectable))
		{
			return;
		}
		
		protectable.removeProtection();
	}
	
	@EventHandler
	public void on(FoodLevelChangeEvent event)
	{
		if(!(event.getEntity() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof Protectable))
		{
			return;
		}
		
		if(event.getFoodLevel() < player.getFoodLevel())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(PlayerLaunchProjectileEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof Protectable protectable))
		{
			return;
		}
		
		protectable.removeProtection();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(EntityShootBowEvent event)
	{
		if(!(event.getEntity() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof Protectable protectable))
		{
			return;
		}
		
		protectable.removeProtection();
	}
}
