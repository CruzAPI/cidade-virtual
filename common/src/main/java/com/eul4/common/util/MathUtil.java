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
	
	public static byte clampToByte(int x)
	{
		if(x > Byte.MAX_VALUE)
		{
			return Byte.MAX_VALUE;
		}
		else if(x < Byte.MIN_VALUE)
		{
			return Byte.MIN_VALUE;
		}
		
		return (byte) x;
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
	
	public static byte addSaturated(byte x, byte y)
	{
		final int r = x + y;
		
		if(r > Byte.MAX_VALUE)
		{
			return Byte.MAX_VALUE;
		}
		else if(r < Byte.MIN_VALUE)
		{
			return Byte.MIN_VALUE;
		}
		
		return (byte) r;
	}
}
