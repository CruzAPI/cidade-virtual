package com.eul4.function;

import com.eul4.exception.OverCapacityException;
import com.eul4.holder.Holder;
import com.eul4.wrapper.TradePreview;

import java.util.List;

@FunctionalInterface
public interface TradePreviewListCallable
<
	N extends Number & Comparable<N>,
	H extends Holder<N>,
	TP extends TradePreview<N, H>
>
{
	List<TP> call() throws OverCapacityException;
}
