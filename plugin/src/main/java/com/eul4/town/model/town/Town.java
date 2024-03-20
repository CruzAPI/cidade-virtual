package com.eul4.town.model.town;

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
	
	static boolean isInsideInitialAvailableRadius(int x, int z)
	{
		return Math.abs(x) <= INITIAL_AVAILABLE_RADIUS && Math.abs(z) <= INITIAL_AVAILABLE_RADIUS;
	}
	
	static boolean isInsideTileRadius(int x, int z)
	{
		return Math.abs(x) <= TOWN_RADIUS && Math.abs(z) <= TOWN_RADIUS;
	}
	
	Map<Block, TownBlock> getTownBlocks();
	TownBlock getTownBlock(Block block);
	Optional<TownBlock> findTownBlock(Block block);
	Location getLocation();
	TownTile getTile(Point point);
	Set<Integer> getHologramsId();
}
