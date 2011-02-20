package com.amdocs.filevalidator.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

import com.amdocs.filevalidator.utils.StringUtils;


@XmlAccessorType(XmlAccessType.FIELD)
public class TypeExtsPair {

	@XmlAttribute(name="allowed-exts")
	private String allowedExtsStr;
	
	@XmlTransient
	private Set<String> allowedExts = null;

	@XmlValue
	private String type;
	
	public TypeExtsPair() {}
	public TypeExtsPair(String type, String...allowedExts) {
		this.type = type;
		this.allowedExts = new HashSet<String>(Arrays.asList(allowedExts));
	}

	
	public String getType() {
		return type;
	}

	public Set<String> getAllowedExts() { 
		if (this.allowedExts == null) { 
			synchronized (this) {
				if (this.allowedExts == null) { 
					HashSet<String> tmp = new HashSet<String>();
					for (String ext : StringUtils.splitString(this.allowedExtsStr)) { 
						if (ext.equals("")) continue;
						tmp.add(ext.toLowerCase());
					}
					this.allowedExts = tmp;
				}
			}
		}
		return this.allowedExts;
	}


	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypeExtsPair other = (TypeExtsPair) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	
	
}
