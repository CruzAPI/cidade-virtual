package com.eul4.model.town.structure;

import com.eul4.enums.Currency;
import com.eul4.holder.CapacitatedHolder;

public interface PhysicalDeposit<N extends Number & Comparable<N>> extends Structure
{
	CapacitatedHolder<N> getHolder();
	Currency getCurrency();
	
	default boolean isEmpty()
	{
		return getHolder().isEmpty();
	}
}
