package com.amdocs.filevalidator.modules;

import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amdocs.filevalidator.utils.UnixFilePermissions;


/**
 * Validates the permissions of the file on the server.
 *  
 * @author zach, rotem
 */
@XmlRootElement(name="unix-file-permissions-module")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnixFilePermissionsModule extends ModuleImpl {

	/** Logger */
	@XmlTransient
	private Logger logger = LoggerFactory.getLogger(this.getClass());


	/** The maximal permissions for user (e.g. "rw-") */
	@XmlElement(name="user-max-permissions")
	private String userMaxPermissions;
	
	/** The maximal permissions for group (e.g. "rw-") */
	@XmlElement(name="group-max-permissions")
	private String groupMaxPermissions;

	/** The maximal permissions for all (e.g. "rw-") */
	@XmlElement(name="all-max-permissions")
	private String allMaxPermissions;

	@XmlTransient
	private UnixFilePermissions allowedPerms;
	
	
	public UnixFilePermissionsModule() { 
		// verify that we run on a unix-based system
		if (File.listRoots().length!=1 || !File.listRoots()[0].getAbsolutePath().equals("/")) { 
			logger.warn("You have enabled UnixFilePermissions module but seem to run under a non-Unix system. /bin/ls is required for this module");
		}
	}
	
	
	@Override
	public boolean validate(File file, boolean isGeneratedFilename) {
		
		if (this.allowedPerms == null) initializePerms();
		
		UnixFilePermissions filePermissions = new UnixFilePermissions(file);
		
		if (this.allowedPerms.compareTo(filePermissions) == 1) {
			logger.info("given file doesn't match the required UNIX permissions");
			return false;
		} else { 
			return true;
		}
		
	}

	
	private synchronized void initializePerms() { 
		if (this.allowedPerms == null) {   // double check to prevent race condition
			this.allowedPerms = new UnixFilePermissions(this.userMaxPermissions, this.groupMaxPermissions, this.allMaxPermissions);
		}
	}
	
	
	
}
