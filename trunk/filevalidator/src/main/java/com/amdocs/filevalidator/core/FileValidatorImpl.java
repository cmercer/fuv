package com.amdocs.filevalidator.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amdocs.filevalidator.config.ConfigBean;
import com.amdocs.filevalidator.config.ConfigManager;
import com.amdocs.filevalidator.modules.Module;
import com.amdocs.filevalidator.securityutilities.FileNameGenerator;

/**
 * FUV implementation of the FileValidator
 * 
 * @author zach, rotem
 */
public class FileValidatorImpl implements FileValidator {

	/** Logger */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/** configuration bean */
	private static ConfigBean config = ConfigManager.getInstance().getConfigBean();
	
	
	/** Singleton */
	private static FileValidatorImpl inst;
	private FileValidatorImpl() {}
	public synchronized static FileValidatorImpl getInstance() {
		if (inst == null) { 
			inst = new FileValidatorImpl();
		}
		return inst;
	}
	
	
	public boolean validate(File file) throws IOException {
		
		logger.info("Validating file : {}", file.getAbsolutePath());
		
		InputStream is = new FileInputStream(file);
		
		// iterate all modules
		for (Module module : config.getModules()) {
			logger.debug("Validating module {}", module.getName());
			if (!module.validate(is, file.getAbsolutePath(), file.getName())) { 
				return false;
			}
		}
		
		return true;
	}
	
	public FileNameGenerator getFileNameGenerator(){
		return config.getFileNameGenerator();
	}

}
