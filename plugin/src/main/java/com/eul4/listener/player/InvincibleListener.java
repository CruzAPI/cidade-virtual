package com.eul4.listener.player;

import com.eul4.Main;
import com.eul4.model.player.Invincible;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

@RequiredArgsConstructor
public class InvincibleListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof Invincible))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void on(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof Invincible))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void on(FoodLevelChangeEvent event)
	{
		if(!(event.getEntity() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof Invincible))
		{
			return;
		}
		
		if(event.getFoodLevel() < player.getFoodLevel())
		{
			event.setCancelled(true);
		}
	}
}
