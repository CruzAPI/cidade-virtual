package com.eul4.listener.player;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.eul4.Main;
import com.eul4.model.player.Attacker;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

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
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void on(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof Attacker attacker))
		{
			return;
		}
		
		Block block = event.getBlock();
		
		TownBlock townBlock = Town.getStaticTownBlock(block);
		Town town = townBlock == null ? null : townBlock.getTown();
		
		if(town == null)
		{
			return;
		}
		
		Town attackedTown = attacker.getTownAttack().getTown();
		
		if(town == attackedTown)
		{
			event.setCancelled(false);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void on(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		Block clickedBlock = event.getClickedBlock();
		
		if(clickedBlock == null
				|| !(plugin.getPlayerManager().get(player) instanceof Attacker attacker))
		{
			return;
		}
		
		TownBlock townBlock = Town.getStaticTownBlock(clickedBlock);
		Town town = townBlock == null ? null : townBlock.getTown();
		
		if(town == null)
		{
			return;
		}
		
		Town attackedTown = attacker.getTownAttack().getTown();
		
		if(town == attackedTown)
		{
			event.setCancelled(false);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void on(PlayerBucketEmptyEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof Attacker attacker))
		{
			return;
		}
		
		TownBlock townBlock = Town.getStaticTownBlock(event.getBlock());
		Town town = townBlock == null ? null : townBlock.getTown();
		
		if(town == null)
		{
			return;
		}
		
		Town attackedTown = attacker.getTownAttack().getTown();
		
		if(town == attackedTown)
		{
			event.setCancelled(false);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void on(PlayerBucketFillEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof Attacker attacker))
		{
			return;
		}
		
		TownBlock townBlock = Town.getStaticTownBlock(event.getBlock());
		Town town = townBlock == null ? null : townBlock.getTown();
		
		if(town == null)
		{
			return;
		}
		
		Town attackedTown = attacker.getTownAttack().getTown();
		
		if(town == attackedTown)
		{
			event.setCancelled(false);
		}
	}
}
