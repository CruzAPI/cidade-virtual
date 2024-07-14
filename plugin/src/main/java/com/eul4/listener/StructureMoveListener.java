package com.eul4.listener;

import com.eul4.Main;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Structure;
import com.fastasyncworldedit.core.FaweAPI;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class StructureMoveListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onBlockCanBuild(BlockCanBuildEvent e)
	{
		final Player player = e.getPlayer();
		
		if(player == null || !(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer))
		{
			return;
		}
		
		final Structure movingStructure = townPlayer.getMovingStructure();
		
		if(movingStructure == null || !player.getInventory().getItemInMainHand().equals(movingStructure.getItem()))
		{
			return;
		}
		
		e.setBuildable(false);
		
		final TownBlock townBlock = townPlayer.getTown().getTownBlock(e.getBlock());
		
		if(townBlock == null)
		{
			townPlayer.sendMessage(PluginMessage.YOU_CAN_NOT_CONSTRUCT_OUTSIDE_YOUR_TOWN);
			return;
		}
		
		try
		{
			movingStructure.finishMove(townBlock);
			player.getInventory().removeItemAnySlot(movingStructure.getItem());
			townPlayer.setMovingStructure(null);
		}
		catch(CannotConstructException ex)
		{
			townPlayer.sendMessage(PluginMessage.STRUCTURE_CAN_NOT_CONSTRUCT_HERE);
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e)
	{
		final Player player = e.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer))
		{
			return;
		}
		
		final Structure movingStructure = townPlayer.getMovingStructure();
		final ItemStack itemStack = e.getItemDrop().getItemStack();
		
		if(movingStructure == null || !itemStack.equals(movingStructure.getItem()))
		{
			return;
		}
		
		e.getItemDrop().remove();
		
		player.getInventory().removeItemAnySlot(movingStructure.getItem());
		player.sendMessage("cancel move");
		
		movingStructure.cancelMove();
	}
}
