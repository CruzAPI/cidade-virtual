package com.eul4.economy;

import com.eul4.exception.OperationException;
import com.eul4.holder.Holder;
import lombok.Getter;

public class Transfer<N extends Number & Comparable<N>>
{
	@Getter
	private final Holder<N> holderFrom;
	@Getter
	private final Holder<N> holderTo;
	@Getter
	private final N amount;
	
	private boolean executed;
	private boolean rollbacked;
	
	private N oldHolderFromBalance;
	private N oldHolderToBalance;
	
	public Transfer
	(
		Holder<N> holderFrom,
		Holder<N> holderTo,
		N amount
	)
	{
		this.holderFrom = holderFrom;
		this.holderTo = holderTo;
		this.amount = amount;
	}
	
	public void execute() throws OperationException
	{
		if(executed)
		{
			return;
		}
		
		executed = true;
		
		oldHolderFromBalance = holderFrom.getBalance();
		oldHolderToBalance = holderTo.getBalance();
		holderFrom.subtract(amount);
		holderTo.add(amount);
	}
	
	void rollback() throws OperationException
	{
		if(!executed || rollbacked)
		{
			return;
		}
		
		rollbacked = true;
		holderFrom.setBalance(oldHolderFromBalance);
		holderTo.setBalance(oldHolderToBalance);
	}
}