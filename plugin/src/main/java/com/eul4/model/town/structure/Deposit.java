package com.eul4.model.town.structure;

import com.eul4.model.craft.town.structure.ResourceStructure;

public interface Deposit<N extends Number> extends ResourceStructure
{
	N getCapacity();
	N getVirtualBalance();
	N getTotalBalance();
}
