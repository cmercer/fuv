package com.amdocs.filevalidator.modules;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.amdocs.filevalidator.config.ConfigManager;
import com.amdocs.filevalidator.core.FileValidator;
import com.amdocs.filevalidator.core.FileValidatorImpl;

import junit.framework.TestCase;

/**
 * Test for AntiVirus Module
 * @author zach, rotem
 *
 */
public class AntiVirusModuleTester extends TestCase {
	
	
	protected FileValidator fv;
	protected AntiVirusModule module;
	
	@Before
	protected void setUp() {
		
		fv = FileValidatorImpl.getInstance();
		
		for (ModuleImpl m : ConfigManager.getInstance().getConfigBean().getModules()) {
			if (m instanceof AntiVirusModule) {
				module = (AntiVirusModule)m;
				break;
			}
		}
		
		if (module == null) {
			fail("There is no anti virus module in the validator");
		}
		
		// put the anti virus module as the only module
		ConfigManager.getInstance().getConfigBean().getModules().clear();
		ConfigManager.getInstance().getConfigBean().getModules().add(module);
	}

	@Test
	public void testScanInnerFiles() {		
		assertFalse("Module shouldnt check inner files", module.scanInnerFiles());
	}

	@Test
	public void testValidate() {
		
		File f = null;
		try {
			f = File.createTempFile("abc", "abc");
			assertTrue("File without viruses", fv.validate(f));
		} catch (IOException e) {
			if (f != null && f.exists()) f.delete();
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
