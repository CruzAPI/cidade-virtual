package com.eul4.wrapper;

import com.eul4.holder.Holder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class TradePreview<N extends Number & Comparable<N>, H extends Holder<N>>
{
	private final H holder;
	private final N preview;
}