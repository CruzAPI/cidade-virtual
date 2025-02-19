package com.eul4.model.town;

import com.eul4.model.town.structure.Structure;
import org.bukkit.block.Block;

import java.util.Optional;

public interface TownBlock
{
	boolean canBuild();
	boolean hasProtection();
	boolean hasStructure();
	
	void setAvailable(boolean value);
	boolean isAvailable();
	Block getBlock();
	Town getTown();
	TownTile getTile();
	Structure getStructure();
	
	void setStructure(Structure structure);
	Optional<Structure> findStructure();
	void reset();
}
