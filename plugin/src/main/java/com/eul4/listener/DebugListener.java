package com.eul4.listener;

import com.eul4.Main;
import com.eul4.enums.Rarity;
import com.eul4.externalizer.filer.BlockDataFiler;
import com.eul4.service.BlockData;
import com.eul4.util.RarityUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.AsyncStructureGenerateEvent;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.structure.Palette;
import org.bukkit.structure.Structure;
import org.bukkit.util.BoundingBox;

import java.util.*;

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
				|| !player.isOp()
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
		
		if(analyzedBlock.getBlockData() instanceof Levelled levelled)
		{
			player.sendMessage("levelled: " + levelled.getLevel() + " min: " + levelled.getMinimumLevel() + " max: " + levelled.getMaximumLevel());
		}
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
