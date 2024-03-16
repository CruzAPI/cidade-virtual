package com.eul4.model.craft.town;

import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class CraftTown implements Town
{
	private final OfflinePlayer owner;
	private final Location location;
	
	private final Map<Block, TownBlock> townBlocks;
	
	public CraftTown(OfflinePlayer owner, Location location)
	{
		this.owner = owner;
		this.location = location;
		
		this.townBlocks = getInitialTownBlocks();
	}
	
	private Map<Block, TownBlock> getInitialTownBlocks()
	{
		if(townBlocks != null)
		{
			return townBlocks;
		}
		
		Map<Block, TownBlock> townBlocks = new HashMap<>();
		
		for(int x = -Town.TOWN_FULL_RADIUS; x <= Town.TOWN_FULL_RADIUS; x++)
		{
			for(int z = -Town.TOWN_FULL_RADIUS; z <= Town.TOWN_FULL_RADIUS; z++)
			{
				Block block = location.getBlock().getRelative(x, 0, z);
				
				townBlocks.put(block, new CraftTownBlock(isInInitialAvailableRadius(x, z)));
			}
		}
		
		return townBlocks;
	}
	
	private boolean isInInitialAvailableRadius(int x, int z)
	{
		return Math.abs(x) <= INITIAL_AVAILABLE_RADIUS
				&& Math.abs(z) <= INITIAL_AVAILABLE_RADIUS;
	}
	
	@Override
	public Map<Block, TownBlock> getTownBlocks()
	{
		return townBlocks;
	}
	
	@Override
	public TownBlock getTownBlock(Block block)
	{
		final Block fixedBlockY = block.getWorld().getBlockAt(block.getX(), Y, block.getZ());
		return townBlocks.get(fixedBlockY);
	}
	
	@Override
	public Optional<TownBlock> findTownBlock(Block block)
	{
		return Optional.ofNullable(getTownBlock(block));
	}
	
	@Override
	public Location getLocation()
	{
		return location.clone();
	}
}
