package com.eul4.model.town;

import com.eul4.model.craft.town.structure.CraftStructure;
import com.eul4.model.town.structure.Structure;
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
	Structure getStructure();
	
	void setStructure(Structure structure);
}
