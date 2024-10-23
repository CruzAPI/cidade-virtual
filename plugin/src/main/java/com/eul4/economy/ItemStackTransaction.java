package com.eul4.economy;

import com.eul4.exception.OperationException;
import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemStackTransaction<N extends Number & Comparable<N>>
{
	@Getter
	private final Transaction<N> transaction;
	
	private final Inventory inventory;
	private final int slot;
	
	@Getter
	private final int amountToConsume;
	
	private final ItemStack originalItemStack;
	private final ItemStack consumedItemStack;
	
	private boolean executed;
	private boolean rollbacked;
	
	public ItemStackTransaction(Transaction<N> transaction, Inventory inventory, int slot, final int amountToConsume)
	{
		Preconditions.checkArgument(amountToConsume >= 0);
		
		this.transaction = transaction;
		this.inventory = inventory;
		this.slot = slot;
		this.originalItemStack = inventory.getItem(slot);
		
		Preconditions.checkArgument(originalItemStack.getAmount() >= amountToConsume);
		
		this.consumedItemStack = originalItemStack.clone().subtract(amountToConsume);
		this.amountToConsume = amountToConsume;
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
			inventory.setItem(slot, consumedItemStack);
			transaction.execute();
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
		
		transaction.rollback();
		inventory.setItem(slot, originalItemStack);
	}
}