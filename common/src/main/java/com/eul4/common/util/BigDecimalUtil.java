package com.eul4.common.util;

import com.eul4.common.exception.NumberFormatException;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigDecimalUtil
{
	public static BigDecimal ONE_CENT = new BigDecimal("0.01");
	
	public static BigDecimal newBigDecimal(String s)
	{
		return newBigDecimal(s, MathContext.UNLIMITED);
	}
	
	public static BigDecimal newBigDecimal(String s, MathContext mathContext)
	{
		try
		{
			return new BigDecimal(s, mathContext);
		}
		catch(java.lang.NumberFormatException e)
		{
			throw new NumberFormatException(e);
		}
	}
}
