package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.util.CommonMessageUtil;
import com.eul4.i18n.PluginMessage;
import com.eul4.i18n.PluginRichMessage;
import com.eul4.world.RaidLevel;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

@RequiredArgsConstructor
public class TrackCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "track";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME };
	
	private final Main plugin;
	
	@RequiredArgsConstructor
	public enum Direction
	{
		NORTH(BlockFace.NORTH, 0, -1)
		{
			@Override
			public Direction getOpositeDirection()
			{
				return SOUTH;
			}
		},
		
		SOUTH(BlockFace.SOUTH, 0, 1)
		{
			@Override
			public Direction getOpositeDirection()
			{
				return NORTH;
			}
		},
		
		EAST(BlockFace.EAST, 1, 0)
		{
			@Override
			public Direction getOpositeDirection()
			{
				return WEST;
			}
		},
		
		WEST(BlockFace.WEST, -1, 0)
		{
			@Override
			public Direction getOpositeDirection()
			{
				return EAST;
			}
		};
		
		private final BlockFace blockFace;
		private final int xMultiplier;
		private final int zMultiplier;
		
		public final String getLabel()
		{
			return name().substring(0, 1);
		}
		
		public abstract Direction getOpositeDirection();
	}
	
	@RequiredArgsConstructor
	public enum TrackType
	{
		COBBLESTONE(Material.OBSIDIAN, Material.COBBLESTONE, Material.STONE, 25, true),
		OBSIDIAN(Material.DIAMOND_BLOCK, Material.OBSIDIAN, Material.GOLD_BLOCK, 25, false),
		IRON(Material.EMERALD_BLOCK, Material.IRON_BLOCK, Material.GOLD_BLOCK, 50, false);
		
		private final Material centerMaterial;
		private final Material armMaterial;
		private final Material endMaterial;
		private final int trackingDistancePerBlock;
		private final boolean temporary;
		
		public static TrackType getTrackerByCenterMaterial(Material centerMaterial)
		{
			for(TrackType trackType : values())
			{
				if(trackType.centerMaterial == centerMaterial)
				{
					return trackType;
				}
			}
			
			return null;
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		List<String> list = new ArrayList<>();
		
		if(!(sender instanceof Player playerSender))
		{
			return list;
		}
		
		if(args.length == 1)
		{
			if("all".startsWith(args[0].toLowerCase()))
			{
				list.add("all");
			}
			
			for(CommonPlayer commonPlayer : plugin.getPlayerManager().getAll())
			{
				Player player = commonPlayer.getPlayer();
				
				if(player.getName().toLowerCase().startsWith(args[0].toLowerCase()) && player.canSee(playerSender) && player != playerSender)
				{
					list.add(player.getName());
				}
			}
		}
		
		return list;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String aliases, String[] args)
	{
		if(!(sender instanceof Player playerSender))
		{
			return true;
		}
		
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(playerSender);
		
		if(!(commonPlayer.getCommonWorld() instanceof RaidLevel))
		{
			commonPlayer.sendMessage(PluginMessage.COMMAND_AVAILABLE_ONLY_IN_RAID);
			return false;
		}
		
		final Collection<? extends Player> targets;
		
		if(args.length == 0)
		{
			targets = plugin.getServer().getOnlinePlayers();
		}
		else if(args.length == 1 && !args[0].equalsIgnoreCase("all"))
		{
			Player target = plugin.getServer().getPlayerExact(args[0]);
			
			if(playerSender == target)
			{
				commonPlayer.sendMessage(PluginMessage.COMMAND_TRACK_YOURSELF, args[0]);
				return false;
			}
			
			if(target == null || !playerSender.canSee(target))
			{
				commonPlayer.sendMessage(CommonMessage.EXCEPTION_PLAYER_NOT_FOUND, args[0]);
				return false;
			}
			
			if(!(plugin.getWorldManager().get(target.getWorld()) instanceof RaidLevel))
			{
				commonPlayer.sendMessage(PluginMessage.COMMAND_TRACK_TARGET_NOT_IN_RAID);
				return false;
			}
			
			targets = Collections.singleton(target);
		}
		else
		{
			commonPlayer.sendMessage(PluginMessage.COMMAND_TRACK_USE_$ALIASES, aliases);
			return false;
		}
		
		Block trackerBlock = playerSender.getLocation().getBlock().getRelative(BlockFace.DOWN);
		TrackType trackType = TrackType.getTrackerByCenterMaterial(trackerBlock.getType());
		
		if(trackType == null)
		{
			commonPlayer.sendMessage(PluginMessage.COMMAND_TRACK_INVALID);
			return false;
		}
		
		int[] range = new int[Direction.values().length];
		
		List<Block> blockList = new ArrayList<>();
		
		blockList.add(trackerBlock);
		
		for(Direction direction : Direction.values())
		{
			List<Block> armList = new ArrayList<>();
			
			for(int j = 1; ; j++)
			{
				Block block = trackerBlock.getRelative(direction.blockFace, j);
				
				range[direction.ordinal()] += trackType.trackingDistancePerBlock;
				
				if(block.getType() == trackType.endMaterial)
				{
					blockList.add(block);
					blockList.addAll(armList);
					break;
				}
				else if(block.getType() != trackType.armMaterial)
				{
					range[direction.ordinal()] = 0;
					break;
				}
				else
				{
					armList.add(block);
				}
			}
		}
		
		if(blockList.size() <= 1)
		{
			commonPlayer.sendMessage(PluginMessage.COMMAND_TRACK_INVALID);
			return false;
		}
		
		if(trackType.temporary)
		{
			blockList.forEach(block -> block.setType(Material.AIR));
		}
		
		Component resultComponent = Component.empty();
		
		final double trackerX = trackerBlock.getLocation().toCenterLocation().getX();
		final double trackerZ = trackerBlock.getLocation().toCenterLocation().getZ();
		
		boolean first = true;
		
		for(Player target : targets)
		{
			if(target == playerSender || !playerSender.canSee(target)
					|| !(plugin.getWorldManager().get(target.getWorld()) instanceof RaidLevel))
			{
				continue;
			}
			
			LinkedHashSet<Direction> targetDirections = new LinkedHashSet<>();
			
			double targetX = target.getLocation().getX();
			double targetZ = target.getLocation().getZ();
			
			for(Direction direction : Direction.values())
			{
				if(targetDirections.contains(direction.getOpositeDirection()))
				{
					continue;
				}
				
				final int i = direction.ordinal();
				
				double absolutRangeX = targetX + range[i] * direction.xMultiplier;
				double absolutRangeZ = targetZ + range[i] * direction.zMultiplier;
				
				if(isInDirectionRange(targetX, trackerX, absolutRangeX) || isInDirectionRange(targetZ, trackerZ, absolutRangeZ))
				{
					targetDirections.add(direction);
				}
			}
			
			StringBuilder targetDirectionLabel = new StringBuilder();
			
			for(Direction direction : targetDirections)
			{
				targetDirectionLabel.append(direction.getLabel());
			}
			
			if(targetDirectionLabel.length() > 0)
			{
				Component comma = Component.text(", ");
				
				TextColor directionColor = targetDirectionLabel.length() == 1
						? NamedTextColor.YELLOW
						: NamedTextColor.GREEN;
				Component directionComponent = Component.text(targetDirectionLabel.toString())
						.color(directionColor);
				
				if(!first)
				{
					resultComponent = resultComponent.append(comma);
				}
				
				first = false;
				
				resultComponent = resultComponent.append(target.displayName())
						.append(Component.text(": "))
						.append(CommonMessageUtil.inParentheses(directionComponent));
			}
		}
		
		Result result = new Result(trackerBlock, range, resultComponent);
		commonPlayer.sendMessage(PluginRichMessage.COMMAND_TRACK_$RESULT, result);
		return true;
	}
	
	private boolean isInDirectionRange(double target, double tracker, double absolutRange)
	{
		return target < absolutRange && target >= tracker || target > absolutRange && target <= tracker;
	}
	
	@RequiredArgsConstructor
	public static class Result
	{
		private final Block trackerCenterBlock;
		private final int[] directionRange;
		private final Component resultComponent;
		
		public int getTrackerX()
		{
			return trackerCenterBlock.getX();
		}
		
		public int getTrackerY()
		{
			return trackerCenterBlock.getY();
		}
		
		public int getTrackerZ()
		{
			return trackerCenterBlock.getZ();
		}
		
		public int getRange(Direction direction)
		{
			try
			{
				return directionRange[direction.ordinal()];
			}
			catch(IndexOutOfBoundsException e)
			{
				return 0;
			}
		}
		
		public Component getResultComponent()
		{
			return resultComponent;
		}
	}
}