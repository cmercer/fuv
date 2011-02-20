package com.amdocs.filevalidator.modules;

import java.io.InputStream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name="test-module")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestModule extends ModuleImpl {

	/** Logger */
	@XmlTransient
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@XmlElement(name="num")
	private int num;
	
	@Override
	public boolean validate(InputStream in, String filePath) {
		logger.warn("something is wrong...");
		return num>5;		
	}

	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	
}
