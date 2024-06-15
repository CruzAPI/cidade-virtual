package com.eul4.model.town.structure;

import com.eul4.model.craft.town.structure.ResourceStructure;

public interface Generator extends ResourceStructure
{
	int getBalance();
	int getCapacity();
	int getDelay();
	void collect();
	void setBalance(int balance);
}
