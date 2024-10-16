package com.eul4.economy;

import com.eul4.event.TransactionExecuteEvent;
import com.eul4.exception.OperationException;
import com.eul4.holder.Holder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Transaction<N extends Number & Comparable<N>>
{
	@Getter
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
	
		new TransactionExecuteEvent(this).callEvent();
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
	
	public Set<Holder<N>> getInvolvedHolders()
	{
		Set<Holder<N>> involvedHolders = new HashSet<>();
		
		for(Transfer<N> transfer : transferList)
		{
			involvedHolders.add(transfer.getHolderFrom());
			involvedHolders.add(transfer.getHolderTo());
		}
		
		return involvedHolders;
	}
	
	//TODO melhorar esse codigo (gambiarra temporraria pra debug)
	public BigDecimal getTotal()
	{
		return transferList.stream().map(transfer -> (BigDecimal) transfer.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}