package com.eul4.wrapper;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CryptoInfoTradePreview extends TradePreview<BigDecimal, CryptoInfo>
{
	private final BigDecimal marketCapPreview;
	private final BigDecimal circulatingSupplyPreview;
	
	public CryptoInfoTradePreview(CryptoInfo holder,
			BigDecimal marketCapPreview,
			BigDecimal circulatingSupplyPreview)
	{
		super(holder, marketCapPreview);
		
		this.marketCapPreview = marketCapPreview;
		this.circulatingSupplyPreview = circulatingSupplyPreview;
	}
}