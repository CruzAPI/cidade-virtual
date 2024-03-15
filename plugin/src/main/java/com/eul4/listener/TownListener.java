package com.eul4.listener;

import com.eul4.Main;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@RequiredArgsConstructor
public class TownListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer))
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
				.ifPresent(townBlock -> event.setCancelled(!townBlock.canBuild()));
	}
}
