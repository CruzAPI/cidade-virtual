package com.eul4.wrapper;

import com.eul4.exception.InvalidCryptoInfoException;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class Trade
{
	private final CryptoInfo cryptoInfo;
	private final BigDecimal amount;
	
	public BigDecimal execute() throws InvalidCryptoInfoException
	{
		return cryptoInfo.trade(amount);
	}
}