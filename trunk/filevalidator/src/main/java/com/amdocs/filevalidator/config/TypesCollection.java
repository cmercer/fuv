package com.amdocs.filevalidator.config;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="types-collection")
@XmlAccessorType(XmlAccessType.FIELD)
public class TypesCollection {
	
	@XmlAttribute(name="name")
	private String collectionName;
	
	@XmlElement(name="type")
	private Set<TypeExtsPair> types;

	
	
	public String getCollectionName() {
		return collectionName;
	}

	public Set<TypeExtsPair> getTypes() {
		return types;
	}
	

}
