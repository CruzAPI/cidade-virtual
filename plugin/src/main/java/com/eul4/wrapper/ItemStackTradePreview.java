package com.eul4.wrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class ItemStackTradePreview
{
	private final List<CryptoInfoTradePreview> previews;
	private final BigDecimal amountToConsume;
	
	public ItemStackTradePreview(CryptoInfoTradePreview preview, BigDecimal amountToConsume)
	{
		this.previews = new ArrayList<>(Collections.singletonList(preview));
		this.amountToConsume = amountToConsume;
	}
	
	public ItemStackTradePreview(BigDecimal amountToConsume)
	{
		this.previews = new ArrayList<>();
		this.amountToConsume = amountToConsume;
	}
	
	public int getAmountToConsume()
	{
		return amountToConsume.setScale(0, RoundingMode.UP).intValueExact();
	}
}