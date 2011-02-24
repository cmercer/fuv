package com.amdocs.filevalidator.utils;

import java.io.File;

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
	public static String extractFileName(String filePath) {		
		return filePath.substring(filePath.lastIndexOf(File.separator) + File.separator.length());
	}
	
}
