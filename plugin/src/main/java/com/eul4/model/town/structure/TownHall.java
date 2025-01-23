package com.eul4.model.town.structure;

import com.eul4.StructureType;
import com.eul4.holder.CapacitatedCrownHolder;
import org.bukkit.Location;

import java.util.Map;

public interface TownHall extends
		ResourceStructure,
		StructureCapacitatedCrownHoldeable,
		CapacitatedCrownTransactionResourceStructure
{
	int getLikeCapacity();
	int getDislikeCapacity();
	
	CapacitatedCrownHolder getCapacitatedCrownHolder();
	void setCapacitatedCrownHolder(CapacitatedCrownHolder capacitatedCrownHolder);
	void setDefaultCapacitatedCrownHolder();
	
	Map<StructureType, Integer> getStructureLimitMap();
	
	Location getSpawnLocation();
}
