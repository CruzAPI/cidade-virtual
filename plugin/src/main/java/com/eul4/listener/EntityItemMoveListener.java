package com.eul4.listener;

import com.eul4.Main;
import com.eul4.model.player.physical.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.wrapper.EntityItemMove;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class EntityItemMoveListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();
		final Block clickedBlock = event.getClickedBlock();
		final BlockFace blockFace = event.getBlockFace();
		final ItemStack item = event.getItem();
		
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK
				|| event.getHand() != EquipmentSlot.HAND
				|| clickedBlock == null
				|| item == null
				|| !(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer))
		{
			return;
		}
		
		final Town town = townPlayer.getTown();
		
		if(town == null)
		{
			return;
		}
		
		Block relative = clickedBlock.getRelative(blockFace);
		EntityItemMove entityItemMove = town.getEntityItemMoveMap().get(item);
		
		if(entityItemMove == null)
		{
			return;
		}
		
		entityItemMove.move(relative);
	}
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event)
	{
		final Player player = event.getPlayer();
		final ItemStack item = event.getItemDrop().getItemStack();
		
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer))
		{
			return;
		}
		
		final Town town = townPlayer.getTown();
		
		if(town == null)
		{
			return;
		}
		
		EntityItemMove entityItemMove = town.getEntityItemMoveMap().get(item);
		
		if(entityItemMove == null)
		{
			return;
		}
		
		event.getItemDrop().remove();
		entityItemMove.remove();
	}
}
