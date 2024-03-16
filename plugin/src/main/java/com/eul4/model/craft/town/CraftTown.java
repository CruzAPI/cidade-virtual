package com.eul4.model.craft.town;

import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class CraftTown implements Town
{
	private final Map<Block, TownBlock> townBlocks;
	
	private final OfflinePlayer owner;
	private final Location location;
	
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


}
