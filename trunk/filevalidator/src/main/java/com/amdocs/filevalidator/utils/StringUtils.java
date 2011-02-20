package com.amdocs.filevalidator.utils;


public class StringUtils {

	
	/** Splits a string to String[] accroding to a set of "normal" delimiters */
	private static String DELIMITERS_PATTERN = " |,|\t|:";
	public static String[] splitString(String str) { 
		return str.split(DELIMITERS_PATTERN);
	}
}
