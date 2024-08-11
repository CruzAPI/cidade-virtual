package com.eul4.common.util;

import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class Preconditions
{
	public static void checkArgument(boolean expression, Supplier<RuntimeException> ex)
	{
		if(!expression)
		{
			throw ex.get();
		}
	}
}
