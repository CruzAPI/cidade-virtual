package com.eul4.service;

import com.eul4.Main;
import com.eul4.economy.Transaction;
import com.eul4.economy.Transfer;
import com.eul4.exception.NegativeBalanceException;
import com.eul4.exception.OverCapacityException;
import com.eul4.holder.CapacitatedHolder;
import com.eul4.holder.Holder;
import com.eul4.model.town.Town;
import com.eul4.wrapper.TradePreview;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class TransactionManager
{
	private final Main plugin;
	
	public
	<
		H1 extends Holder<BigDecimal>
	>
	Transaction<BigDecimal> createTransaction
	(
		H1 holderFrom,
		Town townTo,
		BigDecimal amount
	)
	throws OverCapacityException
	{
		return this.createTransaction
		(
			holderFrom,
			townTo.getCapacitatedCrownHolders(),
			amount
		);
	}
	
	public
	<
		N extends Number & Comparable<N>,
		H1 extends Holder<N>,
		H2 extends Holder<N>
	>
	Transaction<N> createTransaction
	(
		H1 holderFrom,
		List<H2> holdersTo,
		N amount
	)
	throws OverCapacityException
	{
		return createTransaction(new TradePreview<>(holderFrom, amount), holdersTo);
	}
	
	public Transaction<BigDecimal> createTransaction(Town townFrom, Town townTo, BigDecimal amount)
			throws OverCapacityException, NegativeBalanceException
	{
		return createTransaction
		(
			townFrom.createTradePreviewSubtract(amount),
			townTo.getCapacitatedCrownHolders()
		);
	}
	
	public
	<
		N extends Number & Comparable<N>,
		H1 extends Holder<N>,
		H2 extends Holder<N>,
		TP extends TradePreview<N, H1>
	>
	Transaction<N> createTransaction(TP tradePreview, List<H2> holdersTo)
			throws OverCapacityException
	{
		return createTransaction(Collections.singletonList(tradePreview), holdersTo);
	}
	
	public
	<
		N extends Number & Comparable<N>,
		H1 extends Holder<N>,
		H2 extends Holder<N>,
		TP extends TradePreview<N, H1>
	>
	Transaction<N> createTransaction(List<TP> tradePreviews, List<H2> holdersTo)
			throws OverCapacityException
	{
		List<Transfer<N>> transferList = new ArrayList<>();
		Iterator<H2> holderIterator = holdersTo.iterator();
		
		if(!holderIterator.hasNext())
		{
			throw new OverCapacityException();
		}
		
		H2 holder = holderIterator.next();
		N simulatedRemainingCapacity = getRemainingCapacity(holder);
		
		for(TradePreview<N, H1> tradePreview : tradePreviews)
		{
			N preview = tradePreview.getPreview();
			
			while(preview.compareTo(holder.getZeroSample()) > 0)
			{
				while(simulatedRemainingCapacity != null
						&& simulatedRemainingCapacity.compareTo(holder.getZeroSample()) <= 0)
				{
					if(!holderIterator.hasNext())
					{
						throw new OverCapacityException();
					}
					
					holder = holderIterator.next();
					simulatedRemainingCapacity = getRemainingCapacity(holder);
				}
				
				N min = simulatedRemainingCapacity != null && simulatedRemainingCapacity.compareTo(preview) < 0
						? simulatedRemainingCapacity
						: preview;
				
				preview = holder.subtractSample(preview, min);
				simulatedRemainingCapacity = holder.subtractSample(simulatedRemainingCapacity, min);
				
				transferList.add(new Transfer<>(tradePreview.getHolder(), holder, min));
			}
		}
		
		return new Transaction(transferList);
	}
	
	private <N extends Number & Comparable<N>> @Nullable N getRemainingCapacity(Holder<N> holder)
	{
		return holder instanceof CapacitatedHolder capacitatedHolder
				? (N) capacitatedHolder.getRemainingCapacity()
				: null;
	}
}
