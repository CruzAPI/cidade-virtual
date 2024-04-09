package com.eul4.model.town.structure;

public interface Generator extends Structure
{
	int getBalance();
	int getCapacity();
	int getDelay();
	void collect();
}
