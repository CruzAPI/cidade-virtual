package com.eul4.function;

import com.eul4.economy.Transaction;
import com.eul4.exception.OverCapacityException;

@FunctionalInterface
public interface TransactionCreator<N extends Number & Comparable<N>>
{
	Transaction<N> createTransaction(N amount) throws OverCapacityException;
}
