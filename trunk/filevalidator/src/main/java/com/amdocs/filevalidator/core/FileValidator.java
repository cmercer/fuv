package com.amdocs.filevalidator.core;

import java.io.File;
import java.io.IOException;

/**
 * Main interface for the FUV project
 * 
 * @author zach, rotem
 */
public interface FileValidator {
	
	/** Validates a given file */
	public boolean validate(File file) throws IOException;

}
