package com.eul4.listener.world;

import com.eul4.Main;
import com.eul4.common.i18n.MessageArgs;
import com.eul4.common.i18n.Messageable;
import com.eul4.enums.Rarity;
import com.eul4.i18n.PluginMessage;
import com.eul4.util.RarityUtil;
import com.eul4.world.CommonLevel;
import com.eul4.world.NewbieLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@RequiredArgsConstructor
public class CommonLevelListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		Rarity rarity = RarityUtil.getRarity(event.getItemInHand());
		cancelIfNotCommon(player, event.getBlock().getWorld(), event, rarity);
	}
	
	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent event)
	{
		Player player = event.getPlayer();
		Rarity rarity = RarityUtil.getRarity(player.getEquipment().getItem(event.getHand()));
		cancelIfNotCommon(player, event.getBlockClicked().getWorld(), event, rarity);
	}
	
	@EventHandler
	public void onHangPlace(HangingPlaceEvent event)
	{
		Player player = event.getPlayer();
		EquipmentSlot hand = event.getHand();
		
		if(player == null || hand == null)
		{
			return;
		}
		
		Rarity rarity = RarityUtil.getRarity(player.getEquipment().getItem(event.getHand()));
		cancelIfNotCommon(player, event.getBlock().getWorld(), event, rarity);
	}
	
	private void cancelIfNotCommon(@Nullable Player player, World world, Cancellable cancellable, Rarity rarity)
	{
		cancelIfNotCommon(player, PluginMessage.WORLD_BLOCK_RARITY_RESTRICTION.withArgs(), world, cancellable, rarity);
	}
	
	private void cancelIfNotCommon(@Nullable Player player, @NotNull MessageArgs messageArgs, World world, Cancellable cancellable, Rarity rarity)
	{
		cancelIfNotCommon(plugin.getMessageableService().getMessageable(player), messageArgs, world, cancellable, rarity);
	}
	
	private void cancelIfNotCommon(Messageable messageable, MessageArgs messageArgs, World world, Cancellable cancellable, Rarity rarity)
	{
		if(!(plugin.getWorldManager().get(world) instanceof CommonLevel))
		{
			return;
		}
		
		if(rarity != Rarity.COMMON)
		{
			messageable.sendMessage(messageArgs);
			cancellable.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDeathKeepOnlyCommonItems(PlayerDeathEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getWorldManager().get(player.getWorld()) instanceof NewbieLevel newbieLevel))
		{
			return;
		}
		
		boolean hasKeepInventoryGameRule = Optional.ofNullable(newbieLevel.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY)).orElse(false);
		
		if(hasKeepInventoryGameRule)
		{
			return;
		}
		
		event.setKeepInventory(true);
		event.getDrops().clear();
		
		PlayerInventory playerInventory = player.getInventory();
		
		ItemStack[] contents = playerInventory.getContents();
		
		for(int i = 0; i < contents.length; i++)
		{
			ItemStack item = contents[i];
			
			if(item == null)
			{
				continue;
			}
			
			Rarity itemRarity = RarityUtil.getRarity(item);
			
			if(itemRarity == Rarity.COMMON)
			{
				continue;
			}
			
			event.getDrops().add(item);
			playerInventory.setItem(i, null);
		}
	}
}
