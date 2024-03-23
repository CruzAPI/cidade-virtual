package com.eul4.listener;

import com.eul4.Main;
import com.eul4.model.inventory.craft.CraftStructureGui;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

@RequiredArgsConstructor
public class StructureListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onRightClickStructure(PlayerInteractEvent event)
	{
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK
				|| !(plugin.getPlayerManager().get(event.getPlayer()) instanceof TownPlayer townPlayer))
		{
			return;
		}
		
		townPlayer.getTown()
				.findTownBlock(event.getClickedBlock())
				.flatMap(TownBlock::findStructure)
				.ifPresent(structure -> townPlayer.openGui(new CraftStructureGui(townPlayer, structure)));
	}
}
