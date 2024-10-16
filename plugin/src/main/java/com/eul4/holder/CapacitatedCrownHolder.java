package com.eul4.holder;

import com.eul4.exception.NegativeBalanceException;
import com.eul4.exception.OperationException;
import com.eul4.exception.OverCapacityException;
import com.eul4.model.town.Town;
import com.google.common.base.Preconditions;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class CapacitatedCrownHolder implements CrownHolder, CapacitatedHolder<BigDecimal>, TownOwner
{
	private BigDecimal balance;
	private BigDecimal capacity;
	
	@Getter
	private final UUID townUniqueId;
	
	public CapacitatedCrownHolder(Town town)
	{
		this(town.getTownUniqueId());
	}
	
	public CapacitatedCrownHolder(UUID townUniqueId)
	{
		this(townUniqueId, BigDecimal.ZERO);
	}
	
	public CapacitatedCrownHolder(@NotNull UUID townUniqueId, BigDecimal balance)
	{
		this.townUniqueId = Preconditions.checkNotNull(townUniqueId);
		
		this.balance = balance;
		this.capacity = BigDecimal.ZERO;
	}
	
	public BigDecimal getRemainingCapacity()
	{
		return capacity.subtract(balance);
	}
	
	public boolean isFull()
	{
		return balance.compareTo(capacity) >= 0;
	}
	
	@Override
	public boolean isEmpty()
	{
		return balance.compareTo(BigDecimal.ZERO) <= 0;
	}
	
	@Override
	public BigDecimal getBalance()
	{
		return balance;
	}
	
	@Override
	public void setBalance(BigDecimal balance)
	{
		this.balance = balance;
	}
	
	public BigDecimal getCapacity()
	{
		return capacity;
	}
	
	public void setCapacity(BigDecimal capacity)
	{
		this.capacity = capacity;
	}
	
	@Override
	public void add(BigDecimal amount) throws OperationException
	{
		updateBalance(balance.add(amount));
	}
	
	@Override
	public void subtract(BigDecimal amount) throws OperationException
	{
		updateBalance(balance.subtract(amount));
	}
	
	private void updateBalance(BigDecimal newBalance) throws OperationException
	{
		if(newBalance.compareTo(BigDecimal.ZERO) < 0)
		{
			throw new NegativeBalanceException();
		}
		
		if(newBalance.compareTo(capacity) > 0)
		{
			throw new OverCapacityException();
		}
		
		this.balance = newBalance;
	}
}
