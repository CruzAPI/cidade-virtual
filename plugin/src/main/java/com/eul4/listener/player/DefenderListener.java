package com.eul4.listener.player;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.eul4.Main;
import com.eul4.model.player.spiritual.Defender;
import com.eul4.type.player.SpiritualPlayerType;
import com.eul4.wrapper.TownAttack;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

@RequiredArgsConstructor
public class DefenderListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(PlayerRespawnEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof Defender defender))
		{
			return;
		}
		
		event.setRespawnLocation(defender.getPlayer().getLocation());
	}
	@EventHandler
	public void on(PlayerPostRespawnEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof Defender defender))
		{
			return;
		}
		
		defender.findTownAttack().ifPresentOrElse(townAttack ->
		{
			if(!townAttack.canDefenderRespawn())
			{
				plugin.getPlayerManager().register(defender, SpiritualPlayerType.RAID_SPECTATOR);
			}
			else if(townAttack.isNotDefenderRespawnInCooldown())
			{
				defender.reset();
			}
			else
			{
				plugin.getPlayerManager().register(defender, SpiritualPlayerType.DEFENDER_SPECTATOR);
			}
		}, defender::reincarnate);
	}
	
	@EventHandler
	public void on(PlayerDeathEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof Defender defender))
		{
			return;
		}

		if(defender.getTownAttack() != null)
		{
			defender.getTownAttack().canDefenderRespawn(!defender.getTown().getTownHall().isDestroyed());
		}

		defender.findTownAttack().ifPresent(TownAttack::onDefenderDeath);
	}
	
	@EventHandler
	public void on(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof Defender))
		{
			return;
		}
		
		if(!player.isDead())
		{
			player.setHealth(0.0D);
		}
	}
}
