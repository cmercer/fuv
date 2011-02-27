package com.amdocs.filevalidator.config;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.amdocs.filevalidator.modules.AntiVirusModule;
import com.amdocs.filevalidator.modules.FileNameModule;
import com.amdocs.filevalidator.modules.FileTypeModule;
import com.amdocs.filevalidator.modules.UnixFilePermissionsModule;


/**
 * Manages the application-specific configuration.
 * A singleton
 * 
 * @author zach, rotem
 */
public class ConfigManager {

	/** The default configuration file name */
	private static final String DEFAULT_CONFIGURATION_FILE_NAME = "file-validator-config.xml";
	
	
	/** The parsed configuration bean */
	private ConfigBean configBean;
	
	/** Singleton */
	private static ConfigManager inst;
	
	public synchronized static ConfigManager getInstance() { 
		if (inst == null) { 
			inst = new ConfigManager();
		}
		return inst;
	}

	/** 
	 * Private constructor.
	 * Looks for the config file in the classpath, builds the configuration bean.
	 * 
	 */
	private ConfigManager() {
		
		// read the configuration file
		InputStream is = ClassLoader.getSystemResourceAsStream(DEFAULT_CONFIGURATION_FILE_NAME);
		if (is==null) { 
			throw new IllegalArgumentException("Can't find configuration file : " + DEFAULT_CONFIGURATION_FILE_NAME);
		}

		Unmarshaller um = null;
		try { 
			// initialize JAXB
			JAXBContext jc = JAXBContext.newInstance(
					ConfigBean.class, 
					CharStrip.class, 
					FileNameModule.class,
					AntiVirusModule.class,
					FileTypeModule.class,
					UnixFilePermissionsModule.class
			);
			
			um = jc.createUnmarshaller();
		
			// unmarshal the config file
			this.configBean = (ConfigBean)um.unmarshal(is);
			
			
		} catch (JAXBException e) { 
			throw new RuntimeException("Can't create JAXB unmarshaller", e);
		}
		
	}

	/**
	 * Returns the configuration bean
	 */
	public ConfigBean getConfigBean() { 
		return this.configBean;
	}
	
}
