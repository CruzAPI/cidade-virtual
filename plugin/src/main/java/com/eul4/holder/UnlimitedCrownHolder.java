package com.eul4.holder;

import com.eul4.Main;
import com.eul4.model.town.Town;
import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class UnlimitedCrownHolder implements CrownHolder
{
	private final @NotNull Main plugin;
	private final @NotNull UUID uniqueId;
	
	private @NotNull BigDecimal balance;
	
	public UnlimitedCrownHolder(@NotNull Main plugin)
	{
		this(plugin, BigDecimal.ZERO);
	}
	
	public UnlimitedCrownHolder(@NotNull Main plugin, @NotNull BigDecimal balance)
	{
		this(plugin, UUID.randomUUID(), balance);
	}
	
	public UnlimitedCrownHolder(@NotNull Main plugin,
			@NotNull UUID uniqueId,
			@NotNull BigDecimal balance)
	{
		this.plugin = Preconditions.checkNotNull(plugin);
		this.uniqueId = Preconditions.checkNotNull(uniqueId);
		this.balance = Preconditions.checkNotNull(balance);
	}
	
	@Override
	public @NotNull BigDecimal getBalance()
	{
		return balance;
	}
	
	@Override
	public void setBalance(@NotNull BigDecimal balance)
	{
		this.balance = Preconditions.checkNotNull(balance);
	}
	
	@Override
	public void add(BigDecimal amount)
	{
		updateBalance(balance.add(amount));
	}
	
	@Override
	public void subtract(BigDecimal amount)
	{
		updateBalance(balance.subtract(amount));
	}
	
	private void updateBalance(@NotNull BigDecimal newBalance)
	{
		this.balance = Preconditions.checkNotNull(newBalance);
	}
	
	public @NotNull UUID getUniqueId()
	{
		return uniqueId;
	}
	
	@Override
	public String toString()
	{
		return "CapacitatedCrownHolder{uniqueId=" + uniqueId
				+ ", balance=" + balance
				+ '}';
	}
}
