package com.amdocs.filevalidator.securityutilities;


import java.util.List;
import java.util.Random;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.amdocs.filevalidator.config.CharStrip;
import com.amdocs.filevalidator.exceptions.FilenameGenerationException;


/**
 * Utility for generating safe file names
 * @author zach, rotem
 *
 */
@XmlRootElement(name="file-name-generator")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileNameGenerator {
	
	/** Maximum length for file name - for censor method*/
	@XmlElement(name="max-file-name-length")
	private int maxFileNameLength;

	/** File name pattern - for generation method*/
	@XmlElementWrapper(name="name-pattern")
	@XmlElementRef(type=Part.class)
	private List<Part> namePattern;
	
	/** List of allowed character strips - for censor method*/
	@XmlIDREF
	@XmlList
	private List<CharStrip> charStripsToKeep;
	
	/** United list of allowed character strips - for censor method*/
	@XmlTransient
	private String allowedStrip;
	
	@XmlTransient
	private Random rand = new Random();		
	
	/**
	 * Generates a random file name according to the pattern from the configuration
	 * @return fileName
	 * @throws FilenameGenerationException in case of empty name
	 */
	public String generateNewRandomFilename() throws FilenameGenerationException{

		StringBuffer name = new StringBuffer();
		
		// go over all parts of the name
		for (Part part : namePattern) {
			// for each part - generate characters from strip according to length 
			String strip = part.strip.getStrip();
			int stripLen = strip.length();
			for (int i=0; i < part.length; i++){
				int randIndex = rand.nextInt(stripLen);
				name.append(strip.charAt(randIndex));
			}
		}
		
		if (name.length() == 0) {
			throw new FilenameGenerationException("Empty filename was generated. Please check your filename-pattern configuration");
		}
		
		return name.toString();
	}
	
	/**
	 * Censors the given filename: limits the filename length and removes not-allowed characters
	 * @param fileName
	 * @return censor file name
	 * @throws FilenameGenerationException All filename was censored
	 */
	public String censorFilename(String fileName) throws FilenameGenerationException{

		
		// remove extension (if exists)
		String extension = "";
		int extensionIndex = fileName.lastIndexOf('.');
		if (extensionIndex >= 0) {
			extension = fileName.substring(extensionIndex);
			fileName = fileName.substring(0, extensionIndex);
		}	
		
		// create one united allowed strip	
		if (allowedStrip == null) {
			StringBuffer sb = new StringBuffer();
			for (CharStrip cs : charStripsToKeep) {
				sb.append(cs.getStrip());
			}
			allowedStrip = sb.toString();
		}
		
		// go over the characters in the name and leave only the allowed chars
		StringBuffer name = new StringBuffer();
		int size = maxFileNameLength - extension.length();
		int index = 0;
		int insertIndex = 0;
		while (index < fileName.length() && insertIndex < size){
			char c = fileName.charAt(index);
			if (allowedStrip.contains(String.valueOf(c))) {
				name.append(c);			
				insertIndex++;
			}			
			index++;
		}
		
		if (name.length() == 0) {
			throw new FilenameGenerationException("All filename was censored. Please create random filename");
		}

		
		// add back the extension
		name.append(extension);
		
		return name.toString();		
	}
	
	/**
	 * Part of the filename pattern 	 
	 */
	@XmlRootElement(name="part")
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Part {
		
		@XmlElement(name="length")
		private int length;
		
		@XmlIDREF
		private CharStrip strip;
		
	}

	
}
