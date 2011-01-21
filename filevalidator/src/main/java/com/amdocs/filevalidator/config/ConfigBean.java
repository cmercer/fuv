package com.amdocs.filevalidator.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.amdocs.filevalidator.modules.Module;
import com.amdocs.filevalidator.modules.ModuleImpl;

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
	
}
