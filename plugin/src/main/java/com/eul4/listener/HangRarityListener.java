package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.util.ItemStackUtil;
import com.eul4.enums.Rarity;
import com.eul4.externalizer.filer.BlockDataFiler;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.service.BlockData;
import com.eul4.util.RarityUtil;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.world.AsyncStructureGenerateEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class HangRarityListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlaceItemFrame(HangingPlaceEvent event)
	{
		ItemStack itemStack = event.getItemStack();
		
		if(itemStack == null)
		{
			return;
		}
		
		Rarity itemStackRarity = RarityUtil.getRarity(itemStack);
		Hanging hanging = event.getEntity();
		RarityUtil.setRarity(hanging, itemStackRarity);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBreakItemFrame(HangingBreakEvent event)
	{
		Hanging hanging = event.getEntity();
		
		Material material;
		
		if(hanging instanceof ItemFrame)
		{
			material = Material.ITEM_FRAME;
		}
		else if(hanging instanceof Painting)
		{
			material = Material.PAINTING;
		}
		else if(hanging instanceof LeashHitch)
		{
			material = Material.LEAD;
		}
		else
		{
			return;
		}
		
		if(event instanceof HangingBreakByEntityEvent hangingBreakByEntityEvent
				&& hangingBreakByEntityEvent.getRemover() instanceof Player player
				&& player.getGameMode() == GameMode.CREATIVE)
		{
			return;
		}
		
		Rarity hangingRarity = RarityUtil.getRarity(hanging);
		
		ItemStack item = ItemStack.of(material);
		RarityUtil.setRarity(item, hangingRarity);
		
		event.setCancelled(true);
		
		hanging.remove();
		hanging.getWorld().dropItemNaturally(hanging.getLocation(), item);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlace(PlayerItemFrameChangeEvent event)
	{
		PlayerItemFrameChangeEvent.ItemFrameChangeAction action = event.getAction();
		
		ItemFrame itemFrame = event.getItemFrame();
		ItemStack itemStack = event.getItemStack();
		
		Rarity itemFrameRarity = RarityUtil.getRarity(itemFrame);
		Rarity itemStackRarity = RarityUtil.getRarity(itemStack);
		
		if(itemStack.isEmpty() || action != PlayerItemFrameChangeEvent.ItemFrameChangeAction.PLACE)
		{
			return;
		}
		
		Player player = event.getPlayer();
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(itemFrameRarity != itemStackRarity)
		{
			event.setCancelled(true);
			pluginPlayer.sendMessage(PluginMessage.INCOMPATIBLE_RARITY);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onRemove(PlayerItemFrameChangeEvent event)
	{
		PlayerItemFrameChangeEvent.ItemFrameChangeAction action = event.getAction();
		
		ItemFrame itemFrame = event.getItemFrame();
		ItemStack itemStack = event.getItemStack();
		
		Rarity itemFrameRarity = RarityUtil.getRarity(itemFrame);
		
		if(itemStack.isEmpty() || action != PlayerItemFrameChangeEvent.ItemFrameChangeAction.REMOVE)
		{
			return;
		}
		
		RarityUtil.setRarity(itemStack, itemFrameRarity);
	}
}
