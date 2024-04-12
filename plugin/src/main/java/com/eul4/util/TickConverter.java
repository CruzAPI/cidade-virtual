package com.eul4.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TickConverter
{
	private static final int HOUR_IN_TICK = 20 * 60 * 60;
	
	public static int generationPerHour(int perTicks)
	{
		return generationPerHour(1, perTicks);
	}
	
	public static int generationPerHour(int amount, int perTicks)
	{
		if(perTicks == 0)
		{
			return 0;
		}
		
		return HOUR_IN_TICK / perTicks;
	}
}
