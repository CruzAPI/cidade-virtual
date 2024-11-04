package com.eul4.holder;

import com.eul4.Main;
import com.eul4.exception.NegativeBalanceException;
import com.eul4.exception.OperationException;
import com.eul4.exception.OverCapacityException;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class CapacitatedCrownHolder implements CrownHolder,
		CapacitatedHolder<BigDecimal>,
		StructureOwner
{
	private BigDecimal balance;
	private BigDecimal capacity;
	
	private final @NotNull Main plugin;
	
	@Getter
	private final @NotNull UUID townUniqueId;
	@Getter
	private final @NotNull UUID structureUniqueId;
	
	public CapacitatedCrownHolder(Structure structure)
	{
		this
		(
			structure.getTown().getPlugin(),
			structure.getTown().getTownUniqueId(),
			structure.getUUID()
		);
	}
	
	public CapacitatedCrownHolder
	(
		@NotNull Main plugin,
		@NotNull UUID townUniqueId,
		@NotNull UUID structureUniqueId
	)
	{
		this(plugin, townUniqueId, structureUniqueId, BigDecimal.ZERO);
	}
	
	public CapacitatedCrownHolder
	(
		@NotNull Main plugin,
		@NotNull UUID townUniqueId,
		@NotNull UUID structureUniqueId,
		BigDecimal balance
	)
	{
		this.plugin = Preconditions.checkNotNull(plugin);
		this.townUniqueId = Preconditions.checkNotNull(townUniqueId);
		this.structureUniqueId = Preconditions.checkNotNull(structureUniqueId);
		
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
	
	@Override
	public String toString()
	{
		return "CapacitatedCrownHolder{owner="
				+ getOwnerName()
				+ ", structureUniqueId=" + structureUniqueId
				+ ", balance=" + balance
				+ ", capacity=" + capacity
				+ '}';
	}
	
	private String getOwnerName()
	{
		return plugin.getTownManager()
				.findTownByTownUniqueId(townUniqueId)
				.map(Town::getOwner)
				.map(OfflinePlayer::getName)
				.orElse(null);
	}
}
