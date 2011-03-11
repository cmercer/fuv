package com.amdocs.filevalidator.utils;

public class FileNameUtils {

	/**
	 * Extracts the extension from a filename
	 * @param fileName
	 * @return
	 */
	public static String extractFileExtension(String fileName) {
		int dotInd = fileName.lastIndexOf('.');

		return (dotInd > 0 && dotInd < fileName.length()) ? fileName.substring(dotInd + 1) : "";
	}
	

	/**
	 * Returns the filename with out extension.
	 */
	public static String removeExtension(String fileName) { 
		// remove extension (if exists)
		int extension = fileName.lastIndexOf('.');
		if (extension >= 0) {
			return fileName.substring(0, extension);
		}		
		return fileName;
	}
	
}
