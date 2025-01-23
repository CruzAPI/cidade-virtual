package com.eul4.common.util;

import com.eul4.common.exception.NumberFormatException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.ParseException;

public class DecimalFormatUtil
{
	public static Number parse(DecimalFormat decimalFormat, Number number)
	{
		return parse(decimalFormat, decimalFormat.format(number), number);
	}
	
	public static Number parse(DecimalFormat decimalFormat, String source, Number defaultNumber)
	{
		return parse(decimalFormat, true, source, defaultNumber);
	}
	
	public static Number parse(DecimalFormat decimalFormat, boolean isBigDecimal, String source, Number defaultNumber)
	{
		decimalFormat.setParseBigDecimal(isBigDecimal);
		
		try
		{
			return decimalFormat.parse(source);
		}
		catch(ParseException e)
		{
			return defaultNumber;
		}
	}
}
