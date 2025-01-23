package com.eul4.model.town.structure;

import com.eul4.holder.CapacitatedCrownHolder;

import java.math.BigDecimal;

public interface CrownDeposit extends PhysicalDeposit<BigDecimal>, StructureCapacitatedCrownHoldeable
{
	@Override
	CapacitatedCrownHolder getHolder();
	
	//TODO remover esse set da interface, fazer de outro jeito parecido com o TownTile
	void setHolder(CapacitatedCrownHolder holder);
}
