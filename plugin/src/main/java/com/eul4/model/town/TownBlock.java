package com.eul4.model.town;

import com.eul4.model.town.structure.Structure;
import org.bukkit.block.Block;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.Optional;

public interface TownBlock extends Externalizable
{
	boolean canBuild();
	boolean hasStructure();
	
	void setAvailable(boolean value);
	boolean isAvailable();
	Block getBlock();
	Town getTown();
	TownTile getTile();
	Structure getStructure();
	
	void setStructure(Structure structure);
	Optional<Structure> findStructure();
}
