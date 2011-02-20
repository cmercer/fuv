package com.amdocs.filevalidator.modules;

import java.io.InputStream;
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

	@Override
	public boolean validate(InputStream in, String filePath) {
		String fileName = FileNameUtils.extractFileName(filePath);
		
		// remove extension (if exists)
		int extension = fileName.lastIndexOf('.');
		if (extension >= 0) {
			fileName = fileName.substring(0, extension);
		}
		
		// check file length
		logger.debug("File name length (excluding extension) is " + fileName.length() +". Maximum length allowed: " + maxFileNameLength);
		if (fileName.length() > maxFileNameLength) {
			logger.error("File name length is " + fileName.length() +". Maximum length allowed: " + maxFileNameLength);
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
		char[] chars = fileName.toCharArray();		
		for (Character c : chars) {
			if (!allowedStrip.contains(String.valueOf(c))) {
				logger.error("Invalid char in file name: " + c +". Allowed chars: " + allowedStrip);
				return false;			
			}
		}		
		
		return true;
	}

}
