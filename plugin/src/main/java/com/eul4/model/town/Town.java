package com.eul4.model.town;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface Town
{
	int TOWN_RADIUS = 49;
	int TOWN_FULL_RADIUS = 55;
	int TOWN_FULL_DIAMATER = 55 * 2 + 1;
	int INITIAL_AVAILABLE_RADIUS = 13;
	int Y = 49;
	
	Map<Block, TownBlock> getTownBlocks();
	TownBlock getTownBlock(Block block);
	Optional<TownBlock> findTownBlock(Block block);
	Location getLocation();
	TownTile getTile(Point point);
	Set<ArmorStand> getTileHolograms();
}
