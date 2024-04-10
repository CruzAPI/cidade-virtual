package com.eul4.model.town.structure;

import com.eul4.StructureType;

import java.util.Map;

public interface TownHall extends Structure
{
	int getLikeCapacity();
	int getDislikeCapacity();
	Map<StructureType<?, ?>, Integer> getStructureLimitMap();
}
