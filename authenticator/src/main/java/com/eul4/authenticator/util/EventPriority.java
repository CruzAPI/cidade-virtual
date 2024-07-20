package com.eul4.authenticator.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventPriority
{
	public static final byte LOWEST = Byte.MIN_VALUE;
	public static final byte LOW = -32;
	public static final byte NORMAL = 0;
	public static final byte HIGH = 32;
	public static final byte HIGHEST = 64;
	public static final byte MONITOR = Byte.MAX_VALUE;
}
