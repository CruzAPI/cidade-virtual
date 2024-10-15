package com.eul4.economy;

import com.eul4.exception.OperationException;

import java.math.BigDecimal;
import java.util.List;

public class Transaction<N extends Number & Comparable<N>>
{
	private final List<Transfer<N>> transferList;
	
	private boolean executed;
	private boolean rollbacked;
	
	public Transaction(List<Transfer<N>> transferList)
	{
		this.transferList = transferList;
	}
	
	public void tryExecute() throws OperationException
	{
		try
		{
			execute();
		}
		catch(Exception e)
		{
			tryRollback();
		}
	}
	
	void execute() throws OperationException
	{
		if(executed)
		{
			return;
		}
		
		executed = true;
		
		for(Transfer<?> transfer : transferList)
		{
			transfer.execute();
		}
	}
	
	private void tryRollback()
	{
		try
		{
			rollback();
		}
		catch(Exception e)
		{
			e.printStackTrace();//TODO log
		}
	}
	
	void rollback() throws OperationException
	{
		if(!executed || rollbacked)
		{
			return;
		}
		
		rollbacked = true;
		
		for(Transfer<N> transfer : transferList)
		{
			transfer.rollback();
		}
	}
	
	//TODO melhorar esse codigo (gambiarra temporraria pra debug)
	public BigDecimal getTotal()
	{
		return transferList.stream().map(transfer -> (BigDecimal) transfer.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}