package com.eul4.holder;

import com.eul4.exception.OperationException;

public interface Holder<N extends Number & Comparable<N>>
{
	N getBalance();
//	Currency getCurrency();
	void setBalance(N balance) throws OperationException;
	void add(N amount) throws OperationException;
	void subtract(N amount) throws OperationException;
	
	default boolean isEmpty()
	{
		return getBalance().compareTo(getZeroSample()) <= 0;
	}
	
	N getZeroSample();
	N subtractSample(N minuend, N subtrahend);
	N addSample(N augend, N addend);
}
