package com.eul4.holder;

import com.eul4.enums.Currency;
import com.eul4.exception.OperationException;

public interface Holder<N extends Number>
{
	N getBalance();
//	Currency getCurrency();
	void setBalance(N balance) throws OperationException;
	void add(N amount) throws OperationException;
	void subtract(N amount) throws OperationException;
	boolean isEmpty();
}
