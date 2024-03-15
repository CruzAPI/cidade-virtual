package com.eul4.model.town;

import com.eul4.model.craft.town.CraftTownBlock;
import org.bukkit.block.Block;

import java.util.Map;
import java.util.Optional;

public interface Town
{
	int TOWN_RADIUS = 49;
	int TOWN_FULL_RADIUS = 55;
	int TOWN_FULL_DIAMATER = 55 * 2 + 1;
	int Y = 50;
	
	Map<Block, TownBlock> getTownBlocks();
	TownBlock getTownBlock(Block block);
	Optional<TownBlock> findTownBlock(Block block);
}
