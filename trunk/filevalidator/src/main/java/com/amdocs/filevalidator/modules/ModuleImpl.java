package com.amdocs.filevalidator.modules;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * An abstract class, implementing Module. Made for JAXB
 * 
 * @author zach, rotem
 *
 */
public abstract class ModuleImpl implements Module {

	@XmlAttribute(name="scanInnerFiles")
	private boolean scanInnerFiles = true;

	@Override
	public final boolean scanInnerFiles() { 
		return this.scanInnerFiles;
	}
	
	public final String getName() { 
		return this.getClass().getName();
	}
}
