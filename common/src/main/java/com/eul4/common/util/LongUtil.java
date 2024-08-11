package com.eul4.common.util;

import com.eul4.common.exception.NumberFormatException;

public class LongUtil
{
	public static long parseLong(String s)
	{
		try
		{
			return Long.parseLong(s);
		}
		catch(java.lang.NumberFormatException e)
		{
			throw new NumberFormatException(e);
		}
	}
}
