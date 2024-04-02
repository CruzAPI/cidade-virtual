package com.eul4.model.town.structure;

public interface Generator extends Structure, HologramStructure
{
	int getBalance();
	int getMaxBalance();
	void collect();
}
