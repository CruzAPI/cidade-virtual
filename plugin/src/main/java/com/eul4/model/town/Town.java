package com.eul4.model.town;

import com.eul4.Main;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.structure.Structure;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

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
	Main getPlugin();
	
	void setMovingStructure(Structure structure);
	
	void startMovingStructure(Structure structure) throws IOException, CannotConstructException;
	
	void cancelMovingStructure() throws CannotConstructException;
	
	void finishMovingStructure(TownBlock centerTownBlock, int rotation) throws CannotConstructException;
	
	boolean isMovingStructure();
	Structure getMovingStructure();
}
