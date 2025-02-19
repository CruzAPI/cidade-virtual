package com.eul4.model.town.structure;

import com.eul4.StructureType;
import com.eul4.model.craft.town.structure.ResourceStructure;
import org.bukkit.Location;

import java.util.Map;

public interface TownHall extends ResourceStructure
{
	int getLikeCapacity();
	int getDislikeCapacity();
	Map<StructureType, Integer> getStructureLimitMap();
	
	Location getSpawnLocation();
}
