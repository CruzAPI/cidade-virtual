package com.eul4.listener;

import com.eul4.Main;
import com.eul4.enums.Rarity;
import com.eul4.externalizer.filer.BlockDataFiler;
import com.eul4.service.BlockData;
import com.eul4.util.RarityUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class DebugListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void debugEntity(PlayerInteractEntityEvent event)
	{
		Player player = event.getPlayer();
		Entity clickedEntity = event.getRightClicked();
		ItemStack item = player.getInventory().getItemInMainHand();
		
		if
		(
			!player.isOp()
			|| event.getHand() != EquipmentSlot.HAND
			|| item.getType() != Material.NAUTILUS_SHELL
			&& item.getType() != Material.HEART_OF_THE_SEA
		)
		{
			return;
		}
		
		Rarity itemRarity = RarityUtil.getRarity(item);
		
		if(item.getType() == Material.HEART_OF_THE_SEA)
		{
			RarityUtil.setRarity(clickedEntity, itemRarity);
		}
		
		Rarity entityRarity = RarityUtil.getRarity(clickedEntity);
		
		if(clickedEntity instanceof LivingEntity livingEntity)
		{
			player.sendMessage(entityRarity + ":" + clickedEntity.getType() + ":" + livingEntity.getHealth() + "/" + livingEntity.getMaxHealth());
		}
		else
		{
			player.sendMessage(entityRarity + ":" + clickedEntity.getType());
		}
	}
	
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
