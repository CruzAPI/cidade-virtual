package com.eul4.function;

import com.eul4.economy.Transaction;
import com.eul4.exception.OverCapacityException;
import com.eul4.holder.Holder;
import com.eul4.wrapper.TradePreview;

import java.util.List;

@FunctionalInterface
public interface TransactionalResourceFunction
{
	<
		N extends Number & Comparable<N>,
		H1 extends Holder<N>,
		H2 extends Holder<N>,
		TP extends TradePreview<N, H1>
	>
	Transaction<N> createTransaction(List<TP> tradePreviews, List<H2> holdersTo)
			throws OverCapacityException;
}
