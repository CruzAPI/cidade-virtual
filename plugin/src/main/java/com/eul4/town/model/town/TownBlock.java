package com.eul4.town.model.town;

import org.bukkit.block.Block;

public interface TownBlock
{
	boolean canBuild();
	boolean hasStructure();
	
	void setAvailable(boolean value);
	boolean isAvailable();
	Block getBlock();
	Town getTown();
	TownTile getTile();
}
