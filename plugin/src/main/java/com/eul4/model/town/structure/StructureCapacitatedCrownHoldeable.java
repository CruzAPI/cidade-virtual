package com.eul4.model.town.structure;

import com.eul4.holder.CapacitatedCrownHolder;
import com.eul4.holder.CrownHolder;

public interface StructureCapacitatedCrownHoldeable extends StructureCrownHoldeable
{
	CapacitatedCrownHolder getCapacitatedCrownHolder();
	
	default CrownHolder getCrownHolder()
	{
		return getCapacitatedCrownHolder();
	}
}
