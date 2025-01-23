package com.eul4.calculator;

import java.math.BigDecimal;

public class BigDecimalCalculator extends Calculator<BigDecimal>
{
	public static BigDecimalCalculator INSTANCE = new BigDecimalCalculator();
	
	private BigDecimalCalculator()
	{
	
	}
	
	@Override
	public BigDecimal getZeroSample()
	{
		return BigDecimal.ZERO;
	}
	
	@Override
	public BigDecimal add(BigDecimal augend, BigDecimal addend)
	{
		return augend.add(addend);
	}
	
	@Override
	public BigDecimal subtract(BigDecimal minuend, BigDecimal subtrahend)
	{
		return minuend.subtract(subtrahend);
	}
}
