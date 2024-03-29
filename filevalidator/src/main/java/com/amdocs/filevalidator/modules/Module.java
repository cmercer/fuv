package com.amdocs.filevalidator.modules;

import java.io.File;


/**
 * General interface for a module in the system
 * 
 * @author zach, rotem
 */
public interface Module {

	/** Validate an input stream 
	 * @param file TODO
	 * @param isGeneratedFilename true if the simple file name was generated (and it's not the original one)
	 * */
	public boolean validate(File file, boolean isGeneratedFilename);
	
	/** Return a unique name for the module */
	public String getName();
	
	/** Whether the module should run on the inner files too or only on the external file (in case of an archive) */ 
	public boolean scanInnerFiles();
}
