package com.eul4.common.wrapper;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UUIDUtil
{
	public static UUID fromLongArray(long[] bits)
	{
		return new UUID(bits[0], bits[1]);
	}
	
	public static long[] uuidToLongArray(UUID uuid)
	{
		return new long[] { uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() };
	}
}
