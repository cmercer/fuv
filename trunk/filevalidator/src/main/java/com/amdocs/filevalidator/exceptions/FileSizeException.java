package com.amdocs.filevalidator.exceptions;

import java.io.IOException;

public class FileSizeException extends IOException {

	private static final long serialVersionUID = -8432146390083099414L;
	
	public FileSizeException(String message) {
		super(message);
	}

}
