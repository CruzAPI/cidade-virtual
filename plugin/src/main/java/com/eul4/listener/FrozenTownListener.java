package com.eul4.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.eul4.Main;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.BlockInventoryHolder;

import java.util.List;

@RequiredArgsConstructor
public class FrozenTownListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockBreakEvent event)
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockCanBuildEvent event) //TODO: this is not Cancellable
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(StructureGrowEvent event)
	{
		ifFrozenCancelBlockStates(event.getBlocks(), event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(LeavesDecayEvent event)
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockSpreadEvent event)
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockFormEvent event)
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockFromToEvent event)
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(FluidLevelChangeEvent event)
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(PlayerBucketFillEvent event)
	{
		ifFrozenCancel(event.getBlock(), event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(PlayerBucketEmptyEvent event)
	{
		ifFrozenCancel(event.getBlock(), event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockDestroyEvent event)
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockGrowEvent event)
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(MoistureChangeEvent event)
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockFadeEvent event)
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockMultiPlaceEvent event)
	{
		ifFrozenCancelBlockStates(event.getReplacedBlockStates(), event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockPistonExtendEvent event)
	{
		ifFrozenCancel(event);
		ifFrozenCancelBlocks(event.getBlocks(), event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockPistonRetractEvent event)
	{
		ifFrozenCancel(event);
		ifFrozenCancelBlocks(event.getBlocks(), event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockRedstoneEvent event)
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockDispenseEvent event)
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(InventoryMoveItemEvent event)
	{
		if(event.getDestination().getHolder() instanceof BlockInventoryHolder blockInventoryHolder)
		{
			ifFrozenCancel(blockInventoryHolder.getBlock(), event);
		}
		
		if(event.getSource().getHolder() instanceof BlockInventoryHolder blockInventoryHolder)
		{
			ifFrozenCancel(blockInventoryHolder.getBlock(), event);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(EntityExplodeEvent event)
	{
		ifFrozenCancelBlocks(event.blockList(), event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockExplodeEvent event)
	{
		ifFrozenCancelBlocks(event.blockList(), event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(SpongeAbsorbEvent event)
	{
		ifFrozenCancelBlockStates(event.getBlocks(), event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockBurnEvent event)
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(BlockIgniteEvent event)
	{
		ifFrozenCancel(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(PlayerInteractEvent event)
	{
		Block clickedBlock = event.getClickedBlock();
		
		if(clickedBlock != null)
		{
			ifFrozenCancel(clickedBlock, event);
		}
	}
	
	//TODO: Cancel move structure (get/put)
	
	public void ifFrozenCancelBlocks(List<Block> blocks, Cancellable event)
	{
		for(Block block : blocks)
		{
			if(ifFrozenCancel(block, event))
			{
				return;
			}
		}
	}
	
	public void ifFrozenCancelBlockStates(List<BlockState> blockStates, Cancellable event)
	{
		for(BlockState blockState : blockStates)
		{
			if(ifFrozenCancel(blockState.getBlock(), event))
			{
				return;
			}
		}
	}
	
	public void ifFrozenCancel(BlockEvent blockEvent)
	{
		if(blockEvent instanceof Cancellable cancellable)
		{
			ifFrozenCancel(blockEvent.getBlock(), cancellable);
		}
	}
	
	public boolean ifFrozenCancel(Block block, Cancellable event)
	{
		TownBlock townBlock = Town.getStaticTownBlock(block);
		
		if(townBlock == null)
		{
			return false;
		}
		
		Town town = townBlock.getTown();
		
		if(town == null)
		{
			return false;
		}
		
		return ifFrozenCancel(town, event);
	}
	
	public boolean ifFrozenCancel(Town town, Cancellable event)
	{
		if(town.isFrozen())
		{
			event.setCancelled(true);
		}
		
		return event.isCancelled();
	}
}
