package com.eul4.economy;

import com.eul4.exception.OperationException;

import java.math.BigDecimal;
import java.util.List;

public class Transaction
{
	private final List<Transfer<? extends Number>> transferList;
	
	private boolean executed;
	private boolean rollbacked;
	
	public Transaction(List<Transfer<? extends Number>> transferList)
	{
		this.transferList = transferList;
	}
	
	public void execute() throws OperationException
	{
		if(executed)
		{
			return;
		}
		
		executed = true;
		
		try
		{
			for(Transfer<?> transfer : transferList)
			{
				transfer.execute();
			}
		}
		catch(Exception e)
		{
			tryRollback();
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
	
	private void rollback() throws OperationException
	{
		if(!executed || rollbacked)
		{
			return;
		}
		
		rollbacked = true;
		
		for(Transfer<?> transfer : transferList)
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