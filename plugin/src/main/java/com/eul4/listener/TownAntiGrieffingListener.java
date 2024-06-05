package com.eul4.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.eul4.Main;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.eul4.util.BlockUtil.*;

@RequiredArgsConstructor
public class TownAntiGrieffingListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		cancelBlockEventIfIsGriefing(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockPlace(BlockCanBuildEvent event)
	{
		if(hasAntiGrief(event.getBlock()))
		{
			event.setBuildable(false);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(StructureGrowEvent event)
	{
		cancelMultipleBlockStateEventIfIsGriefing(event, event.getBlocks());
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(LeavesDecayEvent event)
	{
		cancelBlockEventIfIsGriefing(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockSpreadEvent event)
	{
		cancelBlockEventIfIsGriefing(event, event.getNewState().getBlock());
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockFormEvent event)
	{
		cancelBlockEventIfIsGriefing(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onLiquidFlow(BlockFromToEvent event)
	{
		Block block = event.getBlock();
		
		if(block.getType() != Material.LAVA
				&& block.getType() != Material.WATER
				&& !(block.getBlockData() instanceof Waterlogged waterlogged
				&& waterlogged.isWaterlogged()))
		{
			return;
		}
		
		cancelBlockEventIfIsGriefing(event, event.getToBlock());
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDragonEggTeleport(BlockFromToEvent event)
	{
		Block block = event.getBlock();
		
		if(block.getType() != Material.DRAGON_EGG)
		{
			return;
		}
		
		Block toBlock = event.getToBlock();
		
		BlockState fromState = block.getState();
		BlockState toState = toBlock.getState();
		
		fromState.setType(toBlock.getType());
		fromState.setBlockData(toBlock.getBlockData());
		
		toState.setType(block.getType());
		toState.setBlockData(block.getBlockData());
		
		cancelMultipleBlockStateEventIfIsGriefing(event, List.of(fromState, toState));
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(FluidLevelChangeEvent event)
	{
		cancelBlockEventIfIsGriefing(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(PlayerBucketEmptyEvent event)
	{
		cancelBlockEventIfIsGriefing(event, event.getBlock());
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(PlayerBucketFillEvent event)
	{
		cancelBlockEventIfIsGriefing(event, event.getBlock());
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockDestroyEvent event)
	{
		cancelBlockEventIfIsGriefing(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockGrowEvent event)
	{
		cancelBlockEventIfIsGriefing(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(MoistureChangeEvent event)
	{
		cancelBlockEventIfIsGriefing(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockFadeEvent event)
	{
		cancelBlockEventIfIsGriefing(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockMultiPlaceEvent event)
	{
		cancelMultipleBlockStateEventIfIsGriefing(event, event.getReplacedBlockStates());
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockPistonExtendEvent event)
	{
		final Block pistonBase = event.getBlock();
		
		final List<Block> blocks = event.getBlocks();
		final List<BlockState> newBlockStates = new ArrayList<>();
		
		final Block pistonHead = pistonBase.getRelative(event.getDirection());
		
		if(blocks.isEmpty())
		{
			cancelBlockEventIfIsGriefing(event, pistonHead);
			return;
		}
		
		for(final Block block : event.getBlocks())
		{
			final Block previous = block.getRelative(event.getDirection().getOppositeFace());
			final Block next = block.getRelative(event.getDirection());
			final BlockState newBlockState = block.getState();
			
			if(pistonBase.equals(previous))
			{
				newBlockState.setType(Material.PISTON_HEAD);
				newBlockState.setBlockData(Material.PISTON_HEAD.createBlockData());
			}
			else if(blocks.contains(previous))
			{
				newBlockState.setType(previous.getType());
				newBlockState.setBlockData(previous.getBlockData());
			}
			else
			{
				newBlockState.setType(Material.AIR);
				newBlockState.setBlockData(Material.AIR.createBlockData());
			}
			
			newBlockStates.add(newBlockState);
			
			if(!blocks.contains(next) && block.getPistonMoveReaction() != PistonMoveReaction.BREAK)
			{
				final BlockState nextNewBlockState = next.getState();
				
				nextNewBlockState.setType(block.getType());
				nextNewBlockState.setBlockData(block.getBlockData());
				
				newBlockStates.add(nextNewBlockState);
			}
		}
		
		cancelMultipleBlockStateEventIfIsGriefing(event, newBlockStates);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockPistonRetractEvent event)
	{
		final Block pistonBase = event.getBlock();
		final Block pistonHead = pistonBase.getRelative(event.getDirection());
		final List<Block> blocks = event.getBlocks();
		final List<BlockState> newBlockStates = new ArrayList<>();
		
		if(blocks.isEmpty())
		{
			cancelBlockEventIfIsGriefing(event, pistonHead);
			return;
		}
		
		for(final Block block : event.getBlocks())
		{
			final Block nearest = block.getRelative(event.getDirection().getOppositeFace());
			final Block furthest = block.getRelative(event.getDirection());
			final BlockState newBlockState = block.getState();
			
			if(blocks.contains(furthest))
			{
				newBlockState.setType(furthest.getType());
				newBlockState.setBlockData(furthest.getBlockData());
			}
			else
			{
				newBlockState.setType(Material.AIR);
				newBlockState.setBlockData(Material.AIR.createBlockData());
			}
			
			newBlockStates.add(newBlockState);
			
			if(!blocks.contains(nearest))
			{
				final BlockState nearestNewBlockState = nearest.getState();

				nearestNewBlockState.setType(block.getType());
				nearestNewBlockState.setBlockData(block.getBlockData());

				newBlockStates.add(nearestNewBlockState);
			}
		}
		
		cancelMultipleBlockStateEventIfIsGriefing(event, newBlockStates);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDispenseBucket(BlockDispenseEvent event)
	{
		Block block = event.getBlock();
		Dispenser dispenser = (Dispenser) block.getBlockData();
		
		ItemStack item = event.getItem();
		
		BlockFace facing = dispenser.getFacing();
		Block relative = block.getRelative(facing);
		
		if(item.getType() == Material.BUCKET && canFillBucket(relative))
		{
			cancelBlockEventIfIsGriefing(event, relative);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDispenseLava(BlockDispenseEvent event)
	{
		Block block = event.getBlock();
		ItemStack item = event.getItem();
		
		Dispenser dispenser = (Dispenser) block.getBlockData();
		
		BlockFace facing = dispenser.getFacing();
		Block relative = block.getRelative(facing);
		
		if(item.getType() == Material.LAVA_BUCKET && !relative.getType().isSolid())
		{
			cancelBlockEventIfIsGriefing(event, relative);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDispenseWater(BlockDispenseEvent event)
	{
		Block block = event.getBlock();
		ItemStack item = event.getItem();
		
		Dispenser dispenser = (Dispenser) block.getBlockData();
		
		BlockFace facing = dispenser.getFacing();
		Block relative = block.getRelative(facing);
		
		if(canBeFilledByWater(relative) && isWaterBucket(item.getType()))
		{
			cancelBlockEventIfIsGriefing(event, relative);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDispensePowderSnow(BlockDispenseEvent event)
	{
		Block block = event.getBlock();
		ItemStack item = event.getItem();
		
		Dispenser dispenser = (Dispenser) block.getBlockData();
		
		BlockFace facing = dispenser.getFacing();
		Block relative = block.getRelative(facing);
		
		if(item.getType() == Material.POWDER_SNOW_BUCKET && relative.getType() == Material.AIR)
		{
			cancelBlockEventIfIsGriefing(event, relative);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(EntityExplodeEvent event)
	{
		event.blockList().removeIf(this::hasAntiGrief);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockExplodeEvent event)
	{
		event.blockList().removeIf(this::hasAntiGrief);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(EntityChangeBlockEvent event)
	{
		cancelBlockEventIfIsGriefing(event, event.getBlock());
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(SpongeAbsorbEvent event)
	{
		cancelMultipleBlockStateEventIfIsGriefing(event, event.getBlocks());
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockBurnEvent event)
	{
		cancelBlockEventIfIsGriefing(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockIgniteEvent event)
	{
		cancelBlockEventIfIsGriefing(event);
	}
	
	private boolean cancelMultipleBlockEventIfIsGriefing(Cancellable cancellable, Collection<Block> blocks)
	{
		for(Block block : blocks)
		{
			if(cancelBlockEventIfIsGriefing(cancellable, block))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean cancelMultipleBlockStateEventIfIsGriefing(Cancellable cancellable, Collection<BlockState> blockStates)
	{
		for(BlockState blockState : blockStates)
		{
			if(cancelBlockEventIfIsGriefing(cancellable, blockState.getBlock()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean cancelBlockEventIfIsGriefing(BlockEvent blockEvent)
	{
		if(blockEvent instanceof Cancellable cancellable)
		{
			return cancelBlockEventIfIsGriefing(cancellable, blockEvent.getBlock());
		}
		
		return false;
	}
	
	private boolean cancelBlockEventIfIsGriefing(Cancellable cancellable, Block block)
	{
		if(hasAntiGrief(block))
		{
			plugin.getLogger().info("[ANTI-GRIEF] event=" + cancellable.getClass().getSimpleName());
			plugin.getLogger().info("[ANTI-GRIEF] blockType=" + block.getType());
			plugin.getLogger().info("[ANTI-GRIEF] blockPos=" + block.getLocation().toVector().toBlockVector());
			cancellable.setCancelled(true);
			return true;
		}
		
		return false;
	}
	
	private boolean hasAntiGrief(Block block)
	{
		return Town.findStaticTownBlock(block)
				.map(TownBlock::canBuild)
				.filter(Boolean.FALSE::equals)
				.isPresent();
	}
}
