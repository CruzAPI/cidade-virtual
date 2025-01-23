package com.eul4.common.util;

import org.apache.commons.lang.WordUtils;

public class CommonWordUtil
{
	public static final String COLON = ":";
	
	public static String inParentheses(String word)
	{
		return "(" + word + ")";
	}
	
	public static String toUpperCaseAndConcatColon(String word)
	{
		return word.toUpperCase().concat(COLON);
	}
	
	public static String concatColon(String word)
	{
		return word.concat(COLON);
	}
	
	public static String capitalizeAndConcatColon(String word)
	{
		return WordUtils.capitalize(word).concat(COLON);
	}
}
