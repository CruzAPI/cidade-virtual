package com.eul4.common.util;

import org.apache.commons.lang.WordUtils;

public class CommonWordUtil
{
	public static final String COLON = ":";
	
	public static String capitalizeAndConcatColon(String word)
	{
		return WordUtils.capitalize(word).concat(COLON);
	}
}
