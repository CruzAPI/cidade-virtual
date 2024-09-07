package com.eul4.common.util;

public class MathUtil
{
	public static long addSaturated(long x, long y)
	{
		final long r = x + y;
		
		if(((r ^ x) & (r ^ y)) < 0)
		{
			return Long.MIN_VALUE - (r >>> (Long.SIZE - 1));
		}
		
		return r;
	}
	
	public static int addSaturated(int x, int y)
	{
		final int r = x + y;
		
		if(((r ^ x) & (r ^ y)) < 0)
		{
			return Integer.MIN_VALUE - (r >>> (Integer.SIZE - 1));
		}
		
		return r;
	}
}
