package com.amdocs.filevalidator.modules;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.tika.io.IOUtils;
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
	public boolean validate(File file, boolean isGeneratedFilename) {
		
		String command = antiVirusPath + " " + file.getAbsolutePath();
		
		Runtime run = Runtime.getRuntime();		
		try {
			
			logger.debug("Going to run AntiVirus: " + command);
			Process proc = run.exec(command);
			proc.waitFor();
			int rc = proc.exitValue();
			logger.debug("AntiVirus rc: " + rc);
			
			if (rc != successRC) {
				String stdout = IOUtils.toString(proc.getInputStream());
				String stderr = IOUtils.toString(proc.getErrorStream());
				logger.info("AntiVirus failed. Exit code=" + rc);
				logger.info("AntiVirus STDOUT : " + stdout);
				logger.info("AntiVirus STDERR : " + stderr);
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
