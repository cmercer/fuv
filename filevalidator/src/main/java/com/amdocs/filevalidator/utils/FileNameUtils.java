package com.amdocs.filevalidator.utils;

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
	

	
}
