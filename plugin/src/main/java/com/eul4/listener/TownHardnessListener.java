package com.eul4.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.eul4.Main;
import com.eul4.event.StructureConstructEvent;
import com.eul4.exception.TownHardnessLimitException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.Structure;
import com.eul4.service.BlockData;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.block.BlockFace.UP;

@RequiredArgsConstructor
public class TownHardnessListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Block block = event.getBlock();
		TownBlock townBlock = Town.getStaticTownBlock(block);
		
		if(townBlock == null)
		{
			return;
		}
		
		BlockData blockData = plugin.getBlockDataLoader().loadBlockDataOrDefault(block);
		
		if(blockData.hasHardness())
		{
			townBlock.getTown().decreaseHardness(block.getType().getHardness());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Block block = event.getBlock();
		TownBlock townBlock = Town.getStaticTownBlock(block);
		
		if(!(plugin.getPlayerManager().get(event.getPlayer()) instanceof TownPlayer townPlayer)
				|| townBlock == null)
		{
			return;
		}
		
		double hardness = townBlock.getTown().getHardness();
		
		BlockData blockData = plugin.getBlockDataLoader().loadBlockDataOrDefault(block);
		
		if(blockData.hasHardness())
		{
			hardness -= event.getBlockReplacedState().getType().getHardness();
			
			if(event.getBlockReplacedState().getBlockData() instanceof Waterlogged waterloggedReplaced
					&& waterloggedReplaced.isWaterlogged()
					&& !(block.getBlockData() instanceof Waterlogged waterloggedBlock
					&& waterloggedBlock.isWaterlogged()))
			{
				Bukkit.broadcastMessage("placing in waterlog");
				hardness -= Material.WATER.getHardness();
			}
		}
		
		hardness += block.getType().getHardness();
		
		try
		{
			townBlock.getTown().setHardness(hardness);
			blockData.hasHardness(true);
		}
		catch(TownHardnessLimitException e)
		{
			event.setCancelled(true);
			townPlayer.sendMessage(PluginMessage.THIS_BLOCK_WILL_EXCEED_HARDNESS_LIMIT);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(StructureGrowEvent event)
	{
		if(event.getWorld() != plugin.getTownWorld())
		{
			return;
		}
		
		Set<Town> townSet = new HashSet<>();
		double hardness = 0.0D;
		
		for(BlockState blockState : event.getBlocks())
		{
			hardness += blockState.getType().getHardness();
			TownBlock townBlock = Town.getStaticTownBlock(blockState.getBlock());
			
			if(townBlock == null)
			{
				event.setCancelled(true);
				return;
			}
			
			townSet.add(townBlock.getTown());
		}
		
		if(townSet.size() != 1)
		{
			event.setCancelled(true);
			return;
		}
		
		Town town = townSet.iterator().next();
		
		try
		{
			town.increaseHardness(hardness);
			
			for(BlockState blockState : event.getBlocks())
			{
				plugin.getBlockDataLoader().loadBlockDataOrDefault(blockState.getBlock()).hasHardness(true);
			}
		}
		catch(TownHardnessLimitException e)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(LeavesDecayEvent event)
	{
		Block block = event.getBlock();
		TownBlock townBlock = Town.getStaticTownBlock(block);
		
		if(townBlock == null)
		{
			return;
		}
		
		BlockData blockData = plugin.getBlockDataLoader().loadBlockDataOrDefault(block);
		
		if(blockData.hasHardness())
		{
			townBlock.getTown().decreaseHardness(block.getType().getHardness());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockSpreadEvent event)
	{
		Block block = event.getBlock();
		TownBlock townBlock = Town.getStaticTownBlock(block);
		
		if(townBlock == null)
		{
			return;
		}
		
		BlockData blockData = plugin.getBlockDataLoader().loadBlockDataOrDefault(block);
		Town town = townBlock.getTown();
		
		double townHardness = town.getHardness();
		
		if(blockData.hasHardness())
		{
			townHardness -= block.getType().getHardness();
		}
		
		try
		{
			townHardness += event.getNewState().getType().getHardness();
			town.setHardness(townHardness);
			blockData.hasHardness(true);
		}
		catch(TownHardnessLimitException e)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockFormEvent event)
	{
		Block block = event.getBlock();
		BlockState newState = event.getNewState();
		
		Bukkit.broadcastMessage("block: " + block.getType() + " h: " + block.getType().getHardness());
		Bukkit.broadcastMessage("newState: " + newState.getType() + " h: " + newState.getType().getHardness());
		
		TownBlock townBlock = Town.getStaticTownBlock(block);
		
		if(townBlock == null)
		{
			return;
		}
		
		BlockData blockData = plugin.getBlockDataLoader().loadBlockDataOrDefault(block);
		Town town = townBlock.getTown();
		
		double townHardness = town.getHardness();
		
		if(blockData.hasHardness())
		{
			townHardness -= block.getType().getHardness();
		}
		
		try
		{
			townHardness += event.getNewState().getType().getHardness();
			town.setHardness(townHardness);
			blockData.hasHardness(true);
		}
		catch(TownHardnessLimitException e)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(BlockFromToEvent event)
	{
		Block block = event.getBlock();
		
		if(block.getType() != Material.LAVA
				&& block.getType() != Material.WATER
				&& !(block.getBlockData() instanceof Waterlogged waterlogged
				&& waterlogged.isWaterlogged()))
		{
			return;
		}
		
		Block toBlock = event.getToBlock();
		TownBlock townBlock = Town.getStaticTownBlock(toBlock);
		
		if(townBlock == null)
		{
			return;
		}
		
		try
		{
			townBlock.getTown().increaseHardness(Material.WATER.getHardness());
			plugin.getBlockDataLoader().loadBlockDataOrDefault(toBlock).hasHardness(true);
		}
		catch(TownHardnessLimitException e)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(FluidLevelChangeEvent event)
	{
		Block block = event.getBlock();
		var newData = event.getNewData();
		
		TownBlock townBlock = Town.getStaticTownBlock(block);
		
		if(townBlock == null)
		{
			return;
		}
		
		double hardness = townBlock.getTown().getHardness();
		
		BlockData blockData = plugin.getBlockDataLoader().loadBlockDataOrDefault(block);
		
		if(blockData.hasHardness())
		{
			hardness -= block.getType().getHardness();
		}
		
		hardness += newData.getMaterial().getHardness();
		
		try
		{
			townBlock.getTown().setHardness(hardness);
			blockData.hasHardness(true);
		}
		catch(TownHardnessLimitException e)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerBucketEmptyEvent event)
	{
		Block block = event.getBlock();
		TownBlock townBlock = Town.getStaticTownBlock(block);
		
		if(townBlock == null)
		{
			return;
		}
		
		double hardness = townBlock.getTown().getHardness();
		
		hardness += Material.WATER.getHardness();
		
		BlockData blockData = plugin.getBlockDataLoader().loadBlockDataOrDefault(block);
		
		if(blockData.hasHardness() && isFilled(block))
		{
			return;
		}
		
		try
		{
			townBlock.getTown().setHardness(hardness);
			blockData.hasHardness(true);
		}
		catch(TownHardnessLimitException e)
		{
			event.setCancelled(true);
		}
	}
	
	private boolean isFilled(Block block)
	{
		return block.getType() == Material.WATER || block.getType() == Material.LAVA
				|| (block.getBlockData() instanceof Waterlogged waterlogged && waterlogged.isWaterlogged());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerBucketFillEvent event)
	{
		Block block = event.getBlock();
		TownBlock townBlock = Town.getStaticTownBlock(block);
		
		if(townBlock == null)
		{
			return;
		}
		
		BlockData blockData = plugin.getBlockDataLoader().loadBlockDataOrDefault(block);
		
		if(blockData.hasHardness())
		{
			townBlock.getTown().decreaseHardness(Material.WATER.getHardness());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(StructureConstructEvent event)
	{
		Structure structure = event.getStructure();
		
		for(TownBlock townBlock : structure.getTownBlocks())
		{
			Block block = townBlock.getBlock();
			
			for(block = block.getWorld().getBlockAt(block.getX(), 0, block.getZ());
					block.getY() < block.getWorld().getMaxHeight(); block = block.getRelative(UP))
			{
				BlockData blockData = plugin.getBlockDataLoader().loadBlockData(block);
				
				if(blockData == null || !blockData.hasHardness())
				{
					continue;
				}
				
				townBlock.getTown().decreaseHardness(getHardnessPlusWaterlogged(block));
				blockData.hasHardness(false);
			}
		}
	}
	
	private double getHardnessPlusWaterlogged(Block block)
	{
		return getHardnessPlusWaterlogged(block.getBlockData());
	}
	
	private double getHardnessPlusWaterlogged(org.bukkit.block.data.BlockData blockData)
	{
		double hardness = blockData.getMaterial().getHardness();
		
		if(blockData instanceof Waterlogged waterlogged && waterlogged.isWaterlogged())
		{
			hardness += Material.WATER.getHardness();
		}
		
		return hardness;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(BlockDestroyEvent event)
	{
		Block block = event.getBlock();
		var newState = event.getNewState();
		
		TownBlock townBlock = Town.getStaticTownBlock(block);
		
		Bukkit.broadcastMessage("destroy:" + block.getType() + ":" + newState.getMaterial());
		
		if(townBlock == null)
		{
			return;
		}
		
		Town town = townBlock.getTown();
		double hardness = town.getHardness();
		
		BlockData blockData = plugin.getBlockDataLoader().loadBlockDataOrDefault(block);
		
		if(blockData.hasHardness())
		{
			hardness -= getHardnessPlusWaterlogged(block);
		}
		
		hardness += getHardnessPlusWaterlogged(newState);
		
		try
		{
			town.setHardness(hardness);
			blockData.hasHardness(true);
		}
		catch(TownHardnessLimitException e)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(BlockGrowEvent event)
	{
	
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(MoistureChangeEvent event)
	{
		changeHardnessToNewState(event, event.getNewState());
	}
	
	private void changeHardnessToNewState(BlockEvent blockEvent, BlockState newState)
	{
		if(!(blockEvent instanceof Cancellable cancellable))
		{
			return;
		}
		
		final Block block = blockEvent.getBlock();
		final TownBlock townBlock = Town.getStaticTownBlock(block);
		
		if(townBlock == null)
		{
			return;
		}
		
		final BlockData blockData = plugin.getBlockDataLoader().loadBlockDataOrDefault(block);
		
		final Town town = townBlock.getTown();
		double hardness = town.getHardness();
		
		if(blockData.hasHardness())
		{
			hardness -= block.getType().getHardness();
		}
		
		hardness += newState.getType().getHardness();
		
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
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(CreatureSpawnEvent event)
	{
		event.setCancelled(true);
	}
}
