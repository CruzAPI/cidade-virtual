package com.eul4.listener.player;

import com.eul4.Main;
import com.eul4.model.player.Fighter;
import com.eul4.model.town.Town;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
	
	@EventHandler
	public void cancelCraft(CraftItemEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof Fighter fighter))
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void cancelOpenContainers(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof Fighter fighter))
		{
			return;
		}
		
		Block clickedBlock = event.getClickedBlock();
		Action action = event.getAction();
		
		if(clickedBlock == null || !action.isRightClick())
		{
			return;
		}
		
		BlockState blockState = clickedBlock.getState();
		
		if(blockState instanceof TileState || Tag.ANVIL.isTagged(clickedBlock.getType()))
		{
			event.setCancelled(true);
		}
	}
}
