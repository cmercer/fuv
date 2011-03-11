package com.amdocs.filevalidator.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="char-strip")
@XmlAccessorType(XmlAccessType.FIELD)
public class CharStrip {
	
	@XmlAttribute(name="stripKey")
	@XmlID
	private String key;
	
	@XmlElement(name="strip")
	private String strip;

	
	@SuppressWarnings("unused")  // needed for JAXB
	private CharStrip(){}
	
	public CharStrip(String key, String strip) {
		this.key = key;
		this.strip = strip;
	}

	public String getKey() {
		return key;
	}

	public String getStrip() {
		return strip;
	}
	
	

}
