package com.eul4.common.util;

import com.eul4.common.exception.NumberFormatException;

public class IntegerUtil
{
	public static int parseInt(String s)
	{
		try
		{
			return Integer.parseInt(s);
		}
		catch(java.lang.NumberFormatException e)
		{
			throw new NumberFormatException(e);
		}
	}
}
