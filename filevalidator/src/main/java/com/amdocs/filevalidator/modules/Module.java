package com.amdocs.filevalidator.modules;

import java.io.InputStream;

/**
 * General interface for a module in the system
 * 
 * @author zach, rotem
 */
public interface Module {

	/** Validate an input stream */
	public boolean validate(InputStream in);
	
	/** Return a unique name for the module */
	public String getName();
}