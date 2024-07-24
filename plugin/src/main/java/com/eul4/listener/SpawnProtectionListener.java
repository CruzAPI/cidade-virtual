package com.eul4.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.eul4.Main;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.world.OverWorld;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SpawnProtectionListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(StructureGrowEvent event)
	{
		cancelIfInSpawn(event.getWorld(), event.getBlocks(), event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(LeavesDecayEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockSpreadEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockFormEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockFromToEvent event)
	{
		if(!cancelIfInSpawn(event))
		{
			cancelIfInSpawn(event.getToBlock(), event);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(FluidLevelChangeEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(PlayerBucketEmptyEvent event)
	{
		cancelIfInSpawn(event.getBlock(), event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(PlayerBucketFillEvent event)
	{
		cancelIfInSpawn(event.getBlock(), event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockDestroyEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockGrowEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(MoistureChangeEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockFadeEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockMultiPlaceEvent event)
	{
		if(!cancelIfInSpawn(event))
		{
			cancelIfInSpawn(event.getBlock().getWorld(), event.getReplacedBlockStates(), event);
		}
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
			if(!cancelIfInSpawn(event) && !cancelIfInSpawn(pistonHead, event))
			{
				return;
			}
			
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
		
		if(!cancelIfInSpawn(event) && !cancelIfInSpawn(pistonHead, event))
		{
			cancelIfInSpawn(event.getBlock().getWorld(), newBlockStates, event);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockPistonRetractEvent event)
	{
		Block pistonHead = event.getBlock().getRelative(event.getDirection().getOppositeFace());
		
		if(!cancelIfInSpawn(event) && !cancelIfInSpawn(pistonHead, event))
		{
			cancelIfInSpawnBlocks(event.getBlock().getWorld(), event.getBlocks(), event);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDispense(BlockDispenseEvent event)
	{
		Block block = event.getBlock();
		Dispenser dispenser = (Dispenser) block.getBlockData();
		BlockFace facing = dispenser.getFacing();
		Block relative = block.getRelative(facing);
		
		cancelIfInSpawn(relative, event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(EntityExplodeEvent event)
	{
		World world = event.getEntity().getWorld();
		
		if(!(plugin.getWorldManager().get(world) instanceof OverWorld overWorld))
		{
			return;
		}
		
		event.blockList().removeIf(overWorld::isSpawn);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockExplodeEvent event)
	{
		World world = event.getBlock().getWorld();
		
		if(!(plugin.getWorldManager().get(world) instanceof OverWorld overWorld))
		{
			return;
		}
		
		event.blockList().removeIf(overWorld::isSpawn);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(EntityChangeBlockEvent event)
	{
		cancelIfInSpawn(event.getBlock(), event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(SpongeAbsorbEvent event)
	{
		if(!cancelIfInSpawn(event))
		{
			cancelIfInSpawn(event.getBlock().getWorld(), event.getBlocks(), event);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockBurnEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockIgniteEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockFertilizeEvent event)
	{
		if(!cancelIfInSpawn(event))
		{
			cancelIfInSpawn(event.getBlock().getWorld(), event.getBlocks(), event);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(SignChangeEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(PlayerInteractEvent event)
	{
		Block clickedBlock = event.getClickedBlock();
		
		if(clickedBlock == null || Tag.DOORS.isTagged(clickedBlock.getType()))
		{
			return;
		}
		
		cancelIfInSpawn(clickedBlock, event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(CreatureSpawnEvent event)
	{
		if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL)
		{
			return;
		}
		
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(HangingBreakEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(HangingPlaceEvent event)
	{
		cancelIfInSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		cancelIfInSpawn(event.getRightClicked().getLocation().getBlock(), event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onItemFrameChange(PlayerItemFrameChangeEvent event)
	{
		cancelIfInSpawn(event.getItemFrame().getLocation().getBlock(), event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onItemFrameChange(PortalCreateEvent event)
	{
		if(cancelIfInSpawn(event.getWorld(), event.getBlocks(), event))
		{
			if(event.getEntity() instanceof Player player
					&& plugin.getPlayerManager().get(player) instanceof PluginPlayer pluginPlayer)
			{
				pluginPlayer.sendMessage(PluginMessage.CAN_NOT_CREATE_PORTAL_HERE);
			}
		}
	}
	
	private <E extends HangingEvent & Cancellable> boolean cancelIfInSpawn(E event)
	{
		return cancelIfInSpawn(event.getEntity().getLocation().getBlock(), event);
	}
	
	private <E extends EntityEvent & Cancellable> boolean cancelIfInSpawn(E event)
	{
		return cancelIfInSpawn(event.getEntity().getLocation().getBlock(), event);
	}
	
	private <E extends BlockEvent & Cancellable> boolean cancelIfInSpawn(E event)
	{
		return cancelIfInSpawn(event.getBlock(), event);
	}
	
	private boolean cancelIfInSpawn(Block block, Cancellable event)
	{
		if(!(plugin.getWorldManager().get(block.getWorld()) instanceof OverWorld overWorld))
		{
			return event.isCancelled();
		}
		
		if(overWorld.isSpawn(block))
		{
			event.setCancelled(true);
		}
		
		return event.isCancelled();
	}
	
	private boolean cancelIfInSpawn(World world, List<BlockState> blockStates, Cancellable event)
	{
		if(!(plugin.getWorldManager().get(world) instanceof OverWorld overWorld))
		{
			return event.isCancelled();
		}
		
		for(BlockState blockState : blockStates)
		{
			if(overWorld.isSpawn(blockState))
			{
				event.setCancelled(true);
				return event.isCancelled();
			}
		}
		
		return event.isCancelled();
	}
	
	private boolean cancelIfInSpawnBlocks(World world, List<Block> blocks, Cancellable event)
	{
		if(!(plugin.getWorldManager().get(world) instanceof OverWorld overWorld))
		{
			return event.isCancelled();
		}
		
		for(Block block : blocks)
		{
			if(overWorld.isSpawn(block))
			{
				event.setCancelled(true);
				return event.isCancelled();
			}
		}
		
		return event.isCancelled();
	}
}
