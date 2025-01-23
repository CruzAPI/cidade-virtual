package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.hologram.Hologram;
import com.eul4.economy.Transaction;
import com.eul4.event.TileInteractEvent;
import com.eul4.event.TransactionExecuteEvent;
import com.eul4.holder.Holder;
import com.eul4.holder.TownOwner;
import com.eul4.model.inventory.craft.CraftConfirmationGui;
import com.eul4.model.player.physical.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.TownTile;
import com.eul4.service.PurchaseV2;
import com.eul4.type.PluginWorldType;
import com.eul4.world.TownWorld;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CUSTOM;

@RequiredArgsConstructor
public class TownListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBucketFill(PlayerBucketFillEvent event)
	{
		Block block = event.getBlock();
		Player player = event.getPlayer();
		
		if(Town.findStaticTownBlock(block)
				.map(TownBlock::getTown)
				.filter(town -> !town.isOwner(player))
				.isPresent())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBucketEmpty(PlayerBucketEmptyEvent event)
	{
		Block block = event.getBlock();
		Player player = event.getPlayer();
		
		if(Town.findStaticTownBlock(block)
				.map(TownBlock::getTown)
				.filter(town -> !town.isOwner(player))
				.isPresent())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		
		if(Town.findStaticTownBlock(block)
				.map(TownBlock::getTown)
				.filter(town -> !town.isOwner(player))
				.isPresent())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
				|| block.getWorld() != PluginWorldType.TOWN_WORLD.getWorld())
		{
			return;
		}
		
		event.setCancelled(true);
		
		Town town = townPlayer.getTown();
		
		if(town == null)
		{
			return;
		}
		
		town.findTownBlock(block)
				.map(TownBlock::hasProtection)
				.ifPresent(event::setCancelled);
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
				|| block.getWorld() != PluginWorldType.TOWN_WORLD.getWorld())
		{
			return;
		}
		
		event.setCancelled(true);
		
		Town town = townPlayer.getTown();
		
		if(town == null)
		{
			return;
		}
		
		town.findTownBlock(block)
				.map(TownBlock::hasProtection)
				.ifPresent(event::setCancelled);
	}
	
	@EventHandler
	public void onTileRightClick(PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
				|| !townPlayer.hasTown())
		{
			return;
		}
		
		final Action action = event.getAction();
		final Block block = event.getClickedBlock();
		
		if(action != Action.RIGHT_CLICK_BLOCK || block == null)
		{
			return;
		}
		
		final Town town = townPlayer.getTown();
		final TownBlock townBlock = town.getTownBlock(block);
		
		if(townBlock == null || townBlock.isAvailable())
		{
			return;
		}
		
		final TownTile tile = townBlock.getTile();
		
		if(tile == null)
		{
			return;
		}
		
		TileInteractEvent tileInteractEvent = new TileInteractEvent(tile);
		plugin.getPluginManager().callEvent(tileInteractEvent);
		
		if(tileInteractEvent.isCancelled())
		{
			return;
		}
		
		PurchaseV2 purchase = new PurchaseV2(townPlayer, tile.calculatePrice(), tile::buy);
		
		townPlayer.openGui(new CraftConfirmationGui(townPlayer)
		{
			@Override
			public void confirm()
			{
				purchase.tryExecutePurchaseValidatingPrice(tile.calculatePrice());
			}
		});
	}
	
	@EventHandler
	public void on(EntitySpawnEvent event)
	{
		Location spawnLocation = event.getLocation();
		Entity entity = event.getEntity();
		
		TownBlock townBlock = Town.getStaticTownBlock(spawnLocation.getBlock());
		Town town = townBlock == null ? null : townBlock.getTown();
		
		if(town == null || !town.isFrozen())
		{
			return;
		}
		
		if(entity instanceof ArmorStand armorStand)
		{
			plugin.getLogger().warning("armorStand! " + Hologram.isHologram(armorStand));
		}
	}
	
	@EventHandler
	public void on(CreatureSpawnEvent event)
	{
		if(!(plugin.getWorldManager().get(event.getEntity().getWorld()) instanceof TownWorld)
				|| event.getEntity() instanceof ArmorStand
				|| event.getSpawnReason() == CUSTOM)
		{
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void updateCrownBalance(TransactionExecuteEvent event)
	{
		Transaction<?> transaction = event.getTransaction();
		Set<Town> updatedTowns = new HashSet<>();
		
		for(Holder<?> involvedHolder : transaction.getInvolvedHolders())
		{
			if(involvedHolder instanceof TownOwner townOwner)
			{
				Town town = plugin.getTownManager().getTownByTownUniqueId(townOwner.getTownUniqueId());
				
				if(updatedTowns.add(town))
				{
					town.updateCrownBalance();
				}
			}
		}
	}
}
