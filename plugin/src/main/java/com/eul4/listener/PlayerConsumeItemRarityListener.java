package com.eul4.listener;

import com.eul4.Main;
import com.eul4.enums.Rarity;
import com.eul4.util.RarityUtil;
import com.eul4.util.SoundUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class PlayerConsumeItemRarityListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onRightClick(PlayerInteractEntityEvent event)
	{
		Entity entity = event.getRightClicked();
		
		Player player = event.getPlayer();
		EquipmentSlot hand = event.getHand();
		final ItemStack item = player.getEquipment().getItem(hand);
		final ItemStack replacement;
		
		final Rarity entityRarity = RarityUtil.getRarity(entity);
		final Rarity itemRarity = RarityUtil.getRarity(item);
		
		if(entity instanceof Cow cow && item.getType() == Material.BUCKET)
		{
			replacement = ItemStack.of(Material.MILK_BUCKET);
		}
		else if(entity instanceof MushroomCow mushroomCow && item.getType() == Material.BOWL)
		{
			replacement = ItemStack.of(Material.MUSHROOM_STEW);
		}
		else
		{
			return;
		}
		
		event.setCancelled(true);
		
		if(entityRarity.compareTo(itemRarity) < 0)
		{
			SoundUtil.playPlong(player);
			return;
		}
		
		RarityUtil.setRarity(replacement, itemRarity);
		collectAndReplace(player, hand, replacement);
	}
	
	private void collectAndReplace(Player player, EquipmentSlot hand, ItemStack replacement)
	{
		ItemStack item = player.getEquipment().getItem(hand);
		
		if(item.getAmount() == 1)
		{
			player.getEquipment().setItem(hand, replacement);
		}
		else
		{
			item.subtract();
			
			for(ItemStack remaining : player.getInventory().addItem(replacement).values())
			{
				((CraftPlayer) player).getHandle().drop(CraftItemStack.asNMSCopy(remaining), false, true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onConsume(PlayerItemConsumeEvent event)
	{
		Rarity itemRarity = RarityUtil.getRarity(event.getItem());
		ItemStack replacement = event.getOriginalReplacement();
		
		if(replacement != null)
		{
			RarityUtil.setRarity(replacement, itemRarity);
			event.setReplacement(replacement);
		}
	}
}
