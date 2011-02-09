package com.amdocs.filevalidator.modules;

import java.io.InputStream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author zach, rotem
 *
 */
@XmlRootElement(name="file-name-module")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileNameModule extends ModuleImpl {
	
	private static final String MODULE_NAME = "FileNameModule";
	
	/** Logger */
	@XmlTransient
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@XmlElement(name="max-file-name-length")
	private int maxFileNameLength;



	@Override
	public boolean validate(InputStream in, String filePath, String fileName){
		
		// remove extension (if exists)
		int extension = fileName.lastIndexOf('.');
		if (extension >= 0) {
			fileName = fileName.substring(0, extension);
		}
		
		if (fileName.length() > maxFileNameLength) {
			logger.error("File name length is " + fileName.length() +". Maximum length allowed: " + maxFileNameLength);
			return false;
		}
		
		// TODO rotem - check with white list		
		return true;
	}

	@Override
	public String getName() { 
		return MODULE_NAME;
	}

}
