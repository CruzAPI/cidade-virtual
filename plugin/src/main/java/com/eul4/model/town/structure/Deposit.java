package com.eul4.model.town.structure;

import com.eul4.model.craft.town.structure.ResourceStructure;

public interface Deposit extends ResourceStructure
{
	int getCapacity();
	int getVirtualBalance();
}
