package com.amdocs.filevalidator.modules;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validate file using AntiVirus
 * @author zach, rotem
 *
 */
@XmlRootElement(name="anti-virus-module")
@XmlAccessorType(XmlAccessType.FIELD)
public class AntiVirusModule extends ModuleImpl {

	
	/** Logger */
	@XmlTransient
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/** Path for AntiVirus script */
	@XmlElement(name="anti-virus-path")
	private String antiVirusPath;

	
	/** Success return code */
	@XmlElement(name="success-rc")
	private int successRC;


	@Override
	public boolean validate(InputStream in, String filePath) {
		
		String command = antiVirusPath + " " + filePath;
		
		Runtime run = Runtime.getRuntime();		
		try {
			
			logger.debug("Going to run AntiVirus: " + command);
			Process proc = run.exec(command);
			proc.waitFor();
			int rc = proc.exitValue();
			logger.debug("AntiVirus rc: " + rc);
			
			if (rc != successRC) {
				logger.error("Exit code from AntiVirus: " + rc);
				return false;
			}
			
		} catch (IOException e) {
			logger.error("Cannot execute AntiVirus", e);
			return false;
		} catch (InterruptedException e) {
			logger.error("Error while executing AntiVirus", e);
			return false;			
		}

		return true;
	}

}
