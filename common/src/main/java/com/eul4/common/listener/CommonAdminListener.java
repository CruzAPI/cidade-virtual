package com.eul4.common.listener;

import com.eul4.common.Common;
import com.eul4.common.model.player.CommonAdmin;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;

@RequiredArgsConstructor
public class CommonAdminListener implements Listener
{
	private final Common plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBucketFill(PlayerBucketFillEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBucketEmpty(PlayerBucketEmptyEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingBreak(HangingBreakByEntityEvent event)
	{
		if(!(event.getRemover() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingPlace(HangingPlaceEvent event)
	{
		Player player = event.getPlayer();
		
		if(player == null ||
				!(plugin.getPlayerManager().get(player) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteractPlayerOpenPlayerInventory(PlayerInteractEntityEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		if(event.getRightClicked() instanceof Player playerRightClicked)
		{
			player.openInventory(playerRightClicked.getInventory());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemFrameChange(PlayerItemFrameChangeEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPickupItem(EntityPickupItemEvent event)
	{
		if(!(event.getEntity() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof CommonAdmin commonAdmin))
		{
			return;
		}
		
		event.setCancelled(!commonAdmin.canBuild());
	}
}
