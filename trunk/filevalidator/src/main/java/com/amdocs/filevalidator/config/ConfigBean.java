package com.amdocs.filevalidator.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.amdocs.filevalidator.modules.ModuleImpl;
import com.amdocs.filevalidator.securityutilities.FileNameGenerator;

/**
 * Main config bean.
 * Contains the application name and the list of modules
 * 
 * @author zach, rotem
 */
@XmlRootElement(name="file-validator-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigBean {

	/** application's name */
	@XmlElement(name="application-name", required=true)
	private String appName;
	
	/** archive recursion depth */
	@XmlElement(name="archive-recursion-depth", required=true)
	private int archiveRecDepth;
	
	/** List of configured modules */
	@XmlElementWrapper(name="modules")
	@XmlElementRef(type=ModuleImpl.class)
	private List<ModuleImpl> modules;

	/** List of configured character strips */
	@XmlElementWrapper(name="char-strips")
	@XmlElementRef(type=CharStrip.class)
	private List<CharStrip> charStrips;
	
	@XmlElementRef(type=FileNameGenerator.class)
	private FileNameGenerator fileNameGenerator;
	
	/** Max file size for size bounded input stream */
	@XmlElement(name="max-file-size")
	private long maxFileSize;
	
	
	/** Types collection mapping */
	@XmlElementWrapper(name="types-collections")
	@XmlElementRef(type=TypesCollection.class)
	private List<TypesCollection> typesCollection;
	
	@XmlTransient
	private Map<String, Set<TypeExtsPair>> mapTypeCollectionToTypes = null;
	
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getArchiveRecDepth() {
		return archiveRecDepth;
	}

	public void setArchiveRecDepth(int archiveRecDepth) {
		this.archiveRecDepth = archiveRecDepth;
	}

	public List<ModuleImpl> getModules() {
		return modules;
	}

	public void setModules(List<ModuleImpl> modules) {
		this.modules = modules;
	}

	public void setCharStrips(List<CharStrip> charStrips) {
		this.charStrips = charStrips;
	}

	public List<CharStrip> getCharStrips() {
		return charStrips;
	}

	public void setFileNameGenerator(FileNameGenerator fileNameGenerator) {
		this.fileNameGenerator = fileNameGenerator;
	}

	public FileNameGenerator getFileNameGenerator() {
		return fileNameGenerator;
	}

	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public long getMaxFileSize() {
		return maxFileSize;
	}
	
	/**
	 * Builds the map between a type-collection's name to its types, if the map doesn't exist
	 * Then returns the list of types for the requested type-collection
	 */
	public Set<TypeExtsPair> getTypesForCollection(String typeCollection) { 
		if (this.mapTypeCollectionToTypes == null) { 
			synchronized (this) {
				if (this.mapTypeCollectionToTypes == null) {
					HashMap<String, Set<TypeExtsPair>> tmp = new HashMap<String, Set<TypeExtsPair>>();
					for (TypesCollection tc : this.typesCollection) { 
						tmp.put(tc.getCollectionName().toLowerCase(), tc.getTypes());
					}
					this.mapTypeCollectionToTypes = tmp;
				}
			}
		}
		return this.mapTypeCollectionToTypes.get(typeCollection.toLowerCase());
	}
}
