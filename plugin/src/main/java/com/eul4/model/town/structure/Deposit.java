package com.eul4.model.town.structure;

public interface Deposit<N extends Number> extends ResourceStructure
{
	N getCapacity();
	N getVirtualBalance();
	N getTotalBalance();
}
