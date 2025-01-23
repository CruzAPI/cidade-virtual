package com.eul4.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.eul4.Main;
import com.eul4.event.StructureConstructEvent;
import com.eul4.exception.TownHardnessLimitException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.physical.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Structure;
import com.eul4.service.BlockData;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eul4.util.BlockUtil.*;
import static org.bukkit.block.BlockFace.UP;

@RequiredArgsConstructor
public class TownHardnessListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		changeHardnessToNewState(event, event.getBlock().getState(),
				isWaterlogged(event.getBlock()) || event.getBlock().getType() == Material.ICE
				? Material.WATER.createBlockData()
				: Material.AIR.createBlockData());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		changeHardnessToNewState(event, event.getBlockReplacedState(), event.getBlock().getBlockData());
		
		if(plugin.getPlayerManager().get(event.getPlayer()) instanceof TownPlayer townPlayer
				&& event.isCancelled())
		{
			townPlayer.sendMessage(PluginMessage.THIS_BLOCK_WILL_EXCEED_HARDNESS_LIMIT);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(StructureGrowEvent event)
	{
		changeMultipleHardnessToNewState(event, event.getBlocks());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(LeavesDecayEvent event)
	{
		changeHardnessToNewState(event, event.getBlock().getState(), Material.AIR.createBlockData());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockSpreadEvent event)
	{
		changeHardnessToNewState(event, event.getNewState());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockFormEvent event)
	{
		changeHardnessToNewState(event, event.getNewState());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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
		
		changeHardnessToNewState(event, event.getToBlock().getState(), event.getBlock().getBlockData());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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
		
		changeMultipleHardnessToNewState(event, List.of(fromState, toState));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(FluidLevelChangeEvent event)
	{
		changeHardnessToNewState(event, event.getBlock().getState(), event.getNewData());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(PlayerBucketEmptyEvent event)
	{
		final org.bukkit.block.data.BlockData newData;
		
		if(event.getBlock().getBlockData() instanceof Waterlogged waterlogged)
		{
			newData = waterlogged.clone();
			((Waterlogged) newData).setWaterlogged(true);
		}
		else
		{
			newData = Material.WATER.createBlockData();
		}
		
		changeHardnessToNewState(event, event.getBlock().getState(), newData);
		
		if(plugin.getPlayerManager().get(event.getPlayer()) instanceof TownPlayer townPlayer
				&& event.isCancelled())
		{
			townPlayer.sendMessage(PluginMessage.THIS_BLOCK_WILL_EXCEED_HARDNESS_LIMIT);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(PlayerBucketFillEvent event)
	{
		changeHardnessToNewState(event, event.getBlock().getState(), Material.AIR.createBlockData());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(StructureConstructEvent event)
	{
		Structure structure = event.getStructure();
		
		for(TownBlock townBlock : structure.getTownBlockSet())
		{
			Block block = townBlock.getBlock();
			
			for(block = block.getWorld().getBlockAt(block.getX(), 0, block.getZ());
					block.getY() < block.getWorld().getMaxHeight(); block = block.getRelative(UP))
			{
				BlockData blockData = plugin.getBlockDataFiler().loadBlockData(block);
				
				if(blockData == null || !blockData.hasHardness())
				{
					continue;
				}
				
				townBlock.getTown().decreaseHardness(getHardnessPlusWaterlogged(block));
				blockData.hasHardness(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockDestroyEvent event)
	{
		changeHardnessToNewState(event, event.getBlock().getState(), event.getNewState());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockGrowEvent event)
	{
		changeHardnessToNewState(event, event.getNewState());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(MoistureChangeEvent event)
	{
		changeHardnessToNewState(event, event.getNewState());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockFadeEvent event)
	{
		changeHardnessToNewState(event, event.getNewState());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockMultiPlaceEvent event)
	{
		changeHardnessToNewState(event,
				event.getReplacedBlockStates().get(1),
				event.getBlock().getBlockData());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockPistonExtendEvent event)
	{
		final Block pistonBase = event.getBlock();
		
		final List<Block> blocks = event.getBlocks();
		final List<BlockState> newBlockStates = new ArrayList<>();
		
		final Block pistonHead = pistonBase.getRelative(event.getDirection());
		
		if(blocks.isEmpty())
		{
			changeHardnessToNewState(event,
					pistonBase.getRelative(event.getDirection()).getState(),
					Material.PISTON_HEAD.createBlockData());
			
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
		
		changeMultipleHardnessToNewState(event, newBlockStates);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockPistonRetractEvent event)
	{
		final Block pistonBase = event.getBlock();
		final Block pistonHead = pistonBase.getRelative(event.getDirection());
		final List<Block> blocks = event.getBlocks();
		final List<BlockState> newBlockStates = new ArrayList<>();
		
		if(blocks.isEmpty())
		{
			BlockState oldState = pistonHead.getState();
			
			oldState.setType(Material.PISTON_HEAD);
			oldState.setBlockData(Material.PISTON_HEAD.createBlockData());
			
			changeHardnessToNewState(event,
					oldState,
					Material.AIR.createBlockData());
			
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
		
		changeMultipleHardnessToNewState(event, newBlockStates);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDispenseBucket(BlockDispenseEvent event)
	{
		Block block = event.getBlock();
		Dispenser dispenser = (Dispenser) block.getBlockData();
		
		ItemStack item = event.getItem();
		
		BlockFace facing = dispenser.getFacing();
		Block relative = block.getRelative(facing);
		
		if(item.getType() == Material.BUCKET && canFillBucket(relative))
		{
			changeHardnessToNewState(event, relative.getState(), getBlockDataUnfilled(relative.getBlockData()));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDispenseLava(BlockDispenseEvent event)
	{
		Block block = event.getBlock();
		ItemStack item = event.getItem();
		
		Dispenser dispenser = (Dispenser) block.getBlockData();
		
		BlockFace facing = dispenser.getFacing();
		Block relative = block.getRelative(facing);
		
		if(item.getType() == Material.LAVA_BUCKET)
		{
			if(relative.getType().isSolid())
			{
				return;
			}
			
			BlockState airState = newState(relative, Material.AIR);
			
			BlockState oldState = isFilled(relative) ? relative.getState() : airState;
			
			changeHardnessToNewState(event, oldState, Material.LAVA.createBlockData());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDispenseWater(BlockDispenseEvent event)
	{
		Block block = event.getBlock();
		ItemStack item = event.getItem();
		
		Dispenser dispenser = (Dispenser) block.getBlockData();
		
		BlockFace facing = dispenser.getFacing();
		Block relative = block.getRelative(facing);
		
		if(canBeFilledByWater(relative) && isWaterBucket(item.getType()))
		{
			BlockState airState = newState(relative, Material.AIR);
			
			BlockState oldState = isFilled(relative) || relative.getBlockData() instanceof Waterlogged
					? relative.getState() : airState;
			
			var newData = getDataFilledWithWater(relative.getBlockData());
			
			changeHardnessToNewState(event, oldState, newData);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDispensePowderSnow(BlockDispenseEvent event)
	{
		Block block = event.getBlock();
		ItemStack item = event.getItem();
		
		Dispenser dispenser = (Dispenser) block.getBlockData();
		
		BlockFace facing = dispenser.getFacing();
		Block relative = block.getRelative(facing);
		
		if(item.getType() == Material.POWDER_SNOW_BUCKET && relative.getType() == Material.AIR)
		{
			changeHardnessToNewState(event, relative.getState(), Material.POWDER_SNOW.createBlockData());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(EntityExplodeEvent event)
	{
		List<BlockState> newBlockStates = new ArrayList<>();
		
		for(Block block : event.blockList())
		{
			newBlockStates.add(newState(block, Material.AIR));
		}
		
		changeMultipleHardnessToNewState(event, newBlockStates);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockExplodeEvent event)
	{
		List<BlockState> newBlockStates = new ArrayList<>();
		
		for(Block block : event.blockList())
		{
			newBlockStates.add(newState(block, Material.AIR));
		}
		
		changeMultipleHardnessToNewState(event, newBlockStates);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(EntityChangeBlockEvent event)
	{
		changeHardnessToNewState(event, event.getBlock().getState(), event.getBlockData());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(SpongeAbsorbEvent event)
	{
		changeMultipleHardnessToNewState(event, event.getBlocks());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockBurnEvent event)
	{
		changeHardnessToNewState(event, newState(event.getBlock(), Material.AIR));
	}
	
	private void changeHardnessToNewState(Cancellable cancellable, BlockState newState)
	{
		changeHardnessToNewState(cancellable, newState.getBlock().getState(), newState.getBlockData());
	}
	
	private void changeHardnessToNewState(Cancellable cancellable,
			BlockState oldState,
			org.bukkit.block.data.BlockData newData)
	{
		plugin.getLogger().finer("event=" + cancellable.getClass().getSimpleName());
		plugin.getLogger().finer("pos=" + oldState.getLocation().toVector().toBlockVector());
		plugin.getLogger().finer("oldState=" + oldState.getBlockData());
		plugin.getLogger().finer("newData=" + newData);
		
		final Block block = oldState.getBlock();
		final TownBlock townBlock = Town.getStaticTownBlock(block);
		
		if(townBlock == null)
		{
			return;
		}
		
		final BlockData blockData = plugin.getBlockDataFiler().loadBlockDataOrDefault(block);
		
		
		final Town town = townBlock.getTown();
		double hardness = town.getHardness();
		final double oldHardness = hardness;
		
		if(blockData.hasHardness())
		{
			hardness -= getHardnessPlusWaterlogged(oldState);
		}
		
		hardness += getHardnessPlusWaterlogged(newData);
		
		plugin.getLogger().finer(String.format("oldHardness=%.2f", oldHardness));
		plugin.getLogger().finer(String.format("newHardness=%.2f", hardness));
		
		try
		{
			townBlock.getTown().setHardness(hardness);
			blockData.hasHardness(true);
		}
		catch(TownHardnessLimitException e)
		{
			cancellable.setCancelled(true);
		}
	}
	
	private void changeMultipleHardnessToNewState(Cancellable cancellable, List<BlockState> newBlockStates)
	{
		Map<Town, Double> townMap = new HashMap<>();
		
		for(BlockState blockState : newBlockStates)
		{
			TownBlock townBlock = Town.getStaticTownBlock(blockState.getBlock());
			
			if(townBlock == null)
			{
				continue;
			}
			
			Town town = townBlock.getTown();
			
			townMap.putIfAbsent(town, town.getHardness());
			townMap.computeIfPresent(town, (townKey, hardness) -> hardness - getHardnessPlusWaterlogged(blockState.getBlock()));
			townMap.computeIfPresent(town, (townKey, hardness) -> hardness + getHardnessPlusWaterlogged(blockState));
		}
		
		if(townMap.isEmpty())
		{
			return;
		}
		
		if(townMap.size() != 1)
		{
			cancellable.setCancelled(true);
			return;
		}
		
		Map.Entry<Town, Double> entry = townMap.entrySet().iterator().next();
		Town town = entry.getKey();
		double hardness = entry.getValue();
		
		plugin.getLogger().finer("multiple-blocks-event=" + cancellable.getClass().getSimpleName());
		plugin.getLogger().finer("size=" + newBlockStates.size());
		plugin.getLogger().finer(String.format("oldHardness=%.2f", town.getHardness()));
		plugin.getLogger().finer(String.format("newHardness=%.2f", hardness));
		
		try
		{
			town.setHardness(hardness);
			
			for(BlockState blockState : newBlockStates)
			{
				plugin.getBlockDataFiler().loadBlockDataOrDefault(blockState.getBlock()).hasHardness(true);
			}
		}
		catch(TownHardnessLimitException e)
		{
			cancellable.setCancelled(true);
		}
	}
}
