package com.eul4.wrapper;

import java.math.BigDecimal;

public class CryptoInfoTradePreview extends TradePreview<BigDecimal, CryptoInfo>
{
	public CryptoInfoTradePreview(CryptoInfo holder, BigDecimal preview)
	{
		super(holder, preview);
	}
}