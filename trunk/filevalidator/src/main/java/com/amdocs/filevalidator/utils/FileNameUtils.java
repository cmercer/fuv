package com.amdocs.filevalidator.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNameUtils {

	/**
	 * Extracts the extension from a filename
	 * @param fileName
	 * @return
	 */
	public static String extractFileExtension(String fileName) {
		int dotInd = fileName.lastIndexOf('.');

		return (dotInd > 0 && dotInd < fileName.length()) ? fileName
				.substring(dotInd + 1) : null;
	}
	
	
	/**
	 * Extracts the filename from a file path (which can include or not a directory before)
	 */
	private static final Pattern EXTRACT_FILE_NAME = Pattern.compile("(.*/)?([^\\/]+)");
	public static String extractFileName(String filePath) { 
		Matcher m = EXTRACT_FILE_NAME.matcher(filePath);
		
		if (!m.matches() || m.groupCount()!=2) { 
			return null;
		} else { 
			return m.group(2);
		}
	}
	
}
