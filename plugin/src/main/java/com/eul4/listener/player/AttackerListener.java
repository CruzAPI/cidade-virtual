package com.eul4.listener.player;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.eul4.Main;
import com.eul4.model.player.Attacker;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

@RequiredArgsConstructor
public class AttackerListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(PlayerRespawnEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof Attacker attacker))
		{
			return;
		}
		
		event.setRespawnLocation(attacker.getPlayer().getLocation());
	}
	@EventHandler
	public void on(PlayerPostRespawnEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof Attacker attacker))
		{
			return;
		}
		
		attacker.reincarnate();
	}
	
	@EventHandler
	public void on(PlayerDeathEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof Attacker attacker))
		{
			return;
		}
		
		attacker.getTownAttack().onAttackerDeath();
	}
	
	@EventHandler
	public void on(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof Attacker attacker))
		{
			return;
		}
		
		if(!player.isDead())
		{
			attacker.getPlayer().setHealth(0.0D);
		}
	}
}
