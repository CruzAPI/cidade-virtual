package com.eul4.listener;

import com.eul4.Main;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.TownTile;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public class TownListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
				|| block.getWorld() != plugin.getTownWorld())
		{
			return;
		}
		
		event.setCancelled(true);
		
		Town town = townPlayer.getTown();
		
		if(town == null)
		{
			return;
		}
		
		town.findTownBlock(block)
				.map(TownBlock::hasProtection)
				.ifPresent(event::setCancelled);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
				|| block.getWorld() != plugin.getTownWorld())
		{
			return;
		}
		
		event.setCancelled(true);
		
		Town town = townPlayer.getTown();
		
		if(town == null)
		{
			return;
		}
		
		town.findTownBlock(block)
				.map(TownBlock::hasProtection)
				.ifPresent(event::setCancelled);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
				|| !townPlayer.hasTown())
		{
			return;
		}
		
		final Action action = event.getAction();
		final Block block = event.getClickedBlock();
		
		if(action != Action.RIGHT_CLICK_BLOCK || block == null)
		{
			return;
		}
		
		final Town town = townPlayer.getTown();
		final TownBlock townBlock = town.getTownBlock(block);
		
		if(townBlock == null || townBlock.isAvailable())
		{
			return;
		}
		
		final TownTile tile = townBlock.getTile();
		
		if(tile == null)
		{
			return;
		}
		
		tile.buy();
	}
}
