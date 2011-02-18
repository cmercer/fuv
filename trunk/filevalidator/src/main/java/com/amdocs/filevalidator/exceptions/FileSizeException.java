package com.amdocs.filevalidator.exceptions;

public class FileSizeException extends RuntimeException {

	private static final long serialVersionUID = -8432146390083099414L;
	
	public FileSizeException(String message) {
		super(message);
	}

}
