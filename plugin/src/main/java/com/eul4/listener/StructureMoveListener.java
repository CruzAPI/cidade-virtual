package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.event.CommonPlayerRegisterEvent;
import com.eul4.common.event.CommonPlayerUnregisterEvent;
import com.eul4.common.wrapper.Pitch;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Structure;
import com.eul4.wrapper.StructureItemMove;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class StructureMoveListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onBlockClick(PlayerInteractEvent e)
	{
		final Player player = e.getPlayer();
		final Block clickedBlock = e.getClickedBlock();
		
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
				|| e.getHand() != EquipmentSlot.HAND
				|| e.getAction() != Action.RIGHT_CLICK_BLOCK
				|| clickedBlock == null)
		{
			return;
		}
		
		ItemStack item = player.getInventory().getItemInMainHand();
		Structure structure = StructureItemMove.getStructure(item, plugin);
		
		if(structure == null)
		{
			return;
		}
		
		e.setCancelled(true);
		
		TownBlock townBlock = structure.getTown().getTownBlock(clickedBlock);
		
		if(townBlock == null)
		{
			townPlayer.sendMessage(PluginMessage.YOU_CAN_NOT_CONSTRUCT_OUTSIDE_YOUR_TOWN);
			return;
		}
		
		try
		{
			if(structure.finishMove(townBlock, getRotation(player)))
			{
				player.playSound(clickedBlock.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, Pitch.max());
			}
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
		
		ItemStack item = e.getItemDrop().getItemStack();
		Structure structure = StructureItemMove.getStructure(item, plugin);
		
		if(structure == null)
		{
			return;
		}
		
		e.getItemDrop().remove();
		structure.removeAllStructureItemMove(player);
		
		try
		{
			structure.cancelMove();
		}
		catch(CannotConstructException ex)
		{
			townPlayer.sendMessage(PluginMessage.STRUCTURE_FAILED_CANCEL_MOVE);
		}
	}
	
	@EventHandler
	public void onCommonPlayerUnregister(CommonPlayerUnregisterEvent e)
	{
		cancelAllStructureItemMoveInInventory(e.getCommonPlayer().getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onCommonPlayerRegister(CommonPlayerRegisterEvent e)
	{
		cancelAllStructureItemMoveInInventory(e.getCommonPlayer().getPlayer());
	}
	
	private void cancelAllStructureItemMoveInInventory(Player player)
	{
		for(final ItemStack content : player.getInventory().getContents())
		{
			StructureItemMove.findStructure(content, plugin).ifPresent(Structure::cancelMoveBlindly);
		}
	}
	
	public static int getRotation(Player player)
	{
		float yaw = player.getLocation().getYaw();
		yaw = (yaw % 360 + 360) % 360;
		
		int rotation = Math.round(yaw / 90) * 90;
		return rotation % 360;
	}
}
