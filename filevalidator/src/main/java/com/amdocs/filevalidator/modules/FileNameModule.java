package com.amdocs.filevalidator.modules;

import java.io.File;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amdocs.filevalidator.config.CharStrip;
import com.amdocs.filevalidator.utils.FileNameUtils;


/**
 * Validate file name (only the simple name without the extension):
 * 1. checks file name size
 * 2. checks characters in name
 * @author zach, rotem
 *
 */
@XmlRootElement(name="file-name-module")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileNameModule extends ModuleImpl {
	
	/** Logger */
	@XmlTransient
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/** Maximum length for file name (excluding extension) */
	@XmlElement(name="max-file-name-length")
	private int maxFileNameLength;

	/** List of allowed character strips */
	@XmlIDREF
	@XmlList
	private List<CharStrip> allowedCharStrips;
	
	@XmlTransient
	private String allowedStrip;

	public List<CharStrip> getAllowedCharStrips() {
		return this.allowedCharStrips;
	}
	
	@Override
	public boolean validate(File file, boolean isGeneratedFilename) {
		String simpleFileName = file.getName();
		
		if (isGeneratedFilename) {
			logger.info("Skipping filename validation for generated filename");
			return true;
		}
		
		// remove extension (if exists)
		simpleFileName = FileNameUtils.removeExtension(simpleFileName);
		
		// check file length
		logger.debug("File name length (excluding extension) is " + simpleFileName.length() +". Maximum length allowed: " + maxFileNameLength);
		if (simpleFileName.length() > maxFileNameLength) {
			logger.info("File name length is " + simpleFileName.length() +". Maximum length allowed: " + maxFileNameLength);
			return false;
		}
		
		// create one united allowed strip
		if (allowedStrip == null) {
			synchronized (this) {
				if (allowedStrip == null) { 
					StringBuffer sb = new StringBuffer();
					for (CharStrip cs : allowedCharStrips) {
						sb.append(cs.getStrip());
					}
					allowedStrip = sb.toString();
				}
			}
		}
		
		// check with white list
		logger.debug("Allowed chars: " + allowedStrip);
		char[] chars = simpleFileName.toCharArray();		
		for (Character c : chars) {
			if (!allowedStrip.contains(String.valueOf(c))) {
				logger.info("Invalid char in file name: " + c +". Allowed chars: " + allowedStrip);
				return false;			
			}
		}		
		
		return true;
	}

}
