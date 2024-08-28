package com.eul4.listener;

import com.eul4.Main;
import com.eul4.externalizer.filer.BlockDataFiler;
import com.eul4.service.BlockData;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class DebugListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void debugBlockData(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		Block clickedBlock = event.getClickedBlock();
		BlockDataFiler blockDataFiler = plugin.getBlockDataFiler();
		ItemStack item = event.getItem();
		
		if(clickedBlock == null
				|| event.getHand() != EquipmentSlot.HAND
				|| item == null
				|| !event.getAction().isRightClick()
				|| item.getType() != Material.NAUTILUS_SHELL)
		{
			return;
		}
		
		Block analyzedBlock = player.isSneaking()
				? clickedBlock.getRelative(event.getBlockFace())
				: clickedBlock;
		
		if(!blockDataFiler.hasBlockData(analyzedBlock))
		{
			player.sendMessage("NO DATA!!!");
			return;
		}
		
		BlockData blockData = blockDataFiler.loadBlockData(analyzedBlock);
		player.sendMessage(blockData.toString());
	}
	
	@EventHandler
	public void damage(EntityDamageEvent event)
	{
//		if(event.getEntity() instanceof Player player)
//		{
//			Bukkit.broadcastMessage("damage: " + event.getDamage());
//			Bukkit.broadcastMessage("final: " + event.getFinalDamage());
//		}
	}
}
