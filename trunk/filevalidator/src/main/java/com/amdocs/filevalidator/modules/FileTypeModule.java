package com.amdocs.filevalidator.modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ContentHandlerDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amdocs.filevalidator.config.ConfigBean;
import com.amdocs.filevalidator.config.ConfigManager;
import com.amdocs.filevalidator.config.TypeExtsPair;
import com.amdocs.filevalidator.utils.FileNameUtils;
import com.amdocs.filevalidator.utils.StringUtils;

/**
 * Validate file's content type
 * 1. checks file name size
 * 2. checks characters in name
 * @author zach, rotem
 *
 */
@XmlRootElement(name="file-type-module")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileTypeModule extends ModuleImpl {

	/** Logger */
	@XmlTransient
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/** Types whitelist */
	@XmlElement(name="allowed-types")
	private String allowedUserTypes;
	
	/** A map between an acceptable type and its acceptable extensions */
	@XmlTransient
	private Map<String, Set<String>> allowedTypes = null;
	
	@XmlElement(name="force-ext-check")
	private String forceExtCheckString;
	
	
	@Override
	public boolean validate(String filePath, String simpleFileName, boolean isGeneratedFilename) {		
		
		logger.debug("FileTypeModule was called for {}", simpleFileName);
		
		// check initialization
		if (this.allowedTypes == null) initializeAllowedTypes();
		logger.debug("AllowedTypes are {}", this.allowedTypes);
		
		ContentHandlerDecorator contenthandler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY, simpleFileName);
		Parser parser = new AutoDetectParser();
		FileInputStream in = null;
		try {
			in = new FileInputStream(new File(filePath));
			parser.parse(in, contenthandler, metadata, new ParseContext());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while determining mime-type from input stream. {}", e.getMessage(), e);
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("Error while closing file. {}", e.getMessage(), e);
				}
			}
		}
		
		String contentType = metadata.get(Metadata.CONTENT_TYPE);
		logger.debug("content type is " + contentType);

		boolean extCheck = shouldForceExtCheck();
		logger.debug( (extCheck ? "" : "not " ) + "forcing ext check");
		
		
		// check if we accept this content type
		if (this.allowedTypes.containsKey(contentType)) { 

			if (extCheck) { 
				if (isGeneratedFilename) { 
					return true;
				} else { 
					return 
					( 	this.allowedTypes.get(contentType)==null ||   // null means we accept all extensions for this type 
						this.allowedTypes.get(contentType).contains(FileNameUtils.extractFileExtension(simpleFileName).toLowerCase()) );
				}
			} else { 
				return true;
			}
			
		} else { 
			return false;
		}
		
	}


	/**
	 * Creates the set of allowed types out of a list of types and type-collections that the user supplied.
	 */
	private synchronized void initializeAllowedTypes() {
		ConfigBean config = ConfigManager.getInstance().getConfigBean();
		
		Map<String, Set<String>> tmp = new HashMap<String, Set<String>>();
		
		for (String userType : StringUtils.splitString(this.allowedUserTypes)) {
			Set<TypeExtsPair> typesSet = config.getTypesForCollection(userType);
			if (typesSet == null) {
				// user gave a type
				tmp.put(userType.toLowerCase(), null);
			} else { 
				for (TypeExtsPair p : typesSet) { 
					tmp.put(p.getType().toLowerCase(), p.getAllowedExts());
				}
			}
		}
		
		this.allowedTypes = tmp;
	}
	
	
	private boolean shouldForceExtCheck() { 
		return 	this.forceExtCheckString!=null && 
				!this.forceExtCheckString.toLowerCase().equals("false") && 
				!this.forceExtCheckString.toLowerCase().equals("no");
	}
	
}
