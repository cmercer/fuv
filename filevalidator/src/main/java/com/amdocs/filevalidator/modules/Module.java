package com.amdocs.filevalidator.modules;


/**
 * General interface for a module in the system
 * 
 * @author zach, rotem
 */
public interface Module {

	/** Validate an input stream 
	 * @param filePath full path and name of the file
	 * @param simpleFileName only the name of the file 
	 * */
	public boolean validate(String filePath, String simpleFileName);
	
	/** Return a unique name for the module */
	public String getName();
	
	/** Whether the module should run on the inner files too or only on the external file (in case of an archive) */ 
	public boolean scanInnerFiles();
}
