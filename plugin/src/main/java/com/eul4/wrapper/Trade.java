package com.eul4.wrapper;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class Trade
{
	private final CryptoInfo cryptoInfo;
	private final BigDecimal amount;
	
	public BigDecimal execute()
	{
		return cryptoInfo.trade(amount);
	}
}