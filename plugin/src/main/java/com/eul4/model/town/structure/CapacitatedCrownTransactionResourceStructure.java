package com.eul4.model.town.structure;

import com.eul4.economy.Transaction;
import com.eul4.exception.InvalidCryptoInfoException;
import com.eul4.exception.OverCapacityException;
import com.eul4.model.town.Town;
import com.eul4.wrapper.CryptoInfo;
import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

import static com.eul4.common.util.BigDecimalUtil.*;

public interface CapacitatedCrownTransactionResourceStructure extends
		TransactionalResourceStructure,
		StructureCapacitatedCrownHoldeable
{
	@SneakyThrows(InvalidCryptoInfoException.class)
	default Transaction<BigDecimal> createStoleCrownTransaction(BigDecimal amount)
			throws OverCapacityException
	{
		Preconditions.checkArgument(amount.compareTo(BigDecimal.ZERO) > 0);
		CryptoInfo fakeCryptoInfo = getFakeCryptoInfo();
		
		Town attackerTown = getTown().getCurrentAttack().getAttacker().getTown();
		
		BigDecimal holderRemainingCapacity = attackerTown.calculateRemainingCrownCapacity();
		
		if(holderRemainingCapacity.compareTo(BigDecimal.ZERO) <= 0)
		{
			throw new OverCapacityException();
		}
		
		final BigDecimal diffPreview = fakeCryptoInfo.previewMarketCapDiff(amount).negate();
		final BigDecimal balance = getCapacitatedCrownHolder().getBalance();
		final BigDecimal transactionAmount = min
		(
			balance,
			min(holderRemainingCapacity, max(diffPreview, ONE_CENT))
		);
		
		return getTown().getPlugin().getTransactionManager().createTransaction
		(
			getCapacitatedCrownHolder(),
			attackerTown.getCapacitatedCrownHolders(),
			transactionAmount
		);
	}
	
	default BigDecimal getAmountOfCrownsToSteal(ItemStack tool)
	{
		//TODO increase multiplier with fortune or stability
		return BigDecimal.ONE;
	}
	
	private CryptoInfo getFakeCryptoInfo()
	{
		CryptoInfo fakeCryptoInfo = new CryptoInfo(getCapacitatedCrownHolder().getCapacity());
		fakeCryptoInfo.setMarketCap(getCapacitatedCrownHolder().getBalance());
		return fakeCryptoInfo;
	}
}
