package com.amdocs.filevalidator.core;

import java.io.File;
import java.io.IOException;

import com.amdocs.filevalidator.securityutilities.FileNameGenerator;

/**
 * Main interface for the FUV project
 * 
 * @author zach, rotem
 */
public interface FileValidator {
	
	/** Validates a given file */
	public boolean validate(File file) throws IOException;
	
	/** Generate file name according to configuration */
	public FileNameGenerator getFileNameGenerator();

}
