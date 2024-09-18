package com.eul4.model.town.structure;

import com.eul4.holder.Holder;

public interface PhysicalDeposit<N extends Number> extends Structure
{
	Holder<N> getHolder();
	
	default boolean isEmpty()
	{
		return getHolder().isEmpty();
	}
}
