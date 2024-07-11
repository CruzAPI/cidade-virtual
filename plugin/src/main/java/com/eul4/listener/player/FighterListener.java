package com.eul4.listener.player;

import com.eul4.Main;
import com.eul4.model.player.Fighter;
import com.eul4.model.town.Town;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.BoundingBox;

@RequiredArgsConstructor
public class FighterListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(PlayerTeleportEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof Fighter fighter))
		{
			return;
		}
		
		Town town = fighter.getAttackedTown();
		
		if(town == null)
		{
			return;
		}
		
		Location to = event.getTo();
		
		BoundingBox boundingBox = BoundingBox.of(to, to);
		
		BoundingBox playerBoundingBox = player.getBoundingBox();
		
		boundingBox.expand(playerBoundingBox.getWidthX() / 2.0D, 0.0D, playerBoundingBox.getWidthZ() / 2.0D);
		boundingBox.expandDirectional(0.0D, playerBoundingBox.getHeight(), 0.0D);
		
		if(!town.getBoundingBoxExcludingWalls().contains(boundingBox))
		{
			event.setCancelled(true);
		}
	}
}
