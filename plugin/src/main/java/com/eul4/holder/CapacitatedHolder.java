package com.eul4.holder;

public interface CapacitatedHolder<N extends Number & Comparable<N>> extends Holder<N>
{
	boolean isFull();
	
	void setCapacity(N capacity);
	N getCapacity();
	
	N getRemainingCapacity();
}
