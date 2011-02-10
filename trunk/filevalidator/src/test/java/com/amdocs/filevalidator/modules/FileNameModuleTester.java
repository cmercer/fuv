package com.amdocs.filevalidator.modules;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.amdocs.filevalidator.config.ConfigManager;
import com.amdocs.filevalidator.core.FileValidator;
import com.amdocs.filevalidator.core.FileValidatorImpl;

/**
 * Tests for the file name module
 * @author zach, rotem
 *
 */
public class FileNameModuleTester extends TestCase {
	
	protected FileValidator fv;
	protected FileNameModule module;
	
	@Before
	protected void setUp() {
		
		fv = FileValidatorImpl.getInstance();
		
		for (ModuleImpl m : ConfigManager.getInstance().getConfigBean().getModules()) {
			if (m instanceof FileNameModule) {
				module = (FileNameModule)m;
				break;
			}
		}
		
		if (module == null) {
			fail("There is no file name module in the validator");
		}
		
		// put the file name module as the only module
		ConfigManager.getInstance().getConfigBean().getModules().clear();
		ConfigManager.getInstance().getConfigBean().getModules().add(module);
		
		System.out.println("Module name: " + module.getName());
	}

	@Test
	public void testScanInnerFiles() {		
		assertTrue("Module should check inner files", module.scanInnerFiles());
	}

	@Test
	public void testValidate() {

		try {
			File f1 = File.createTempFile("abc", "abc");
			assertTrue("short name without extension should pass", fv.validate(f1));			
			
			File f2 = File.createTempFile("def", "abc.doc");
			assertTrue("short name with extension should pass", fv.validate(f2));
			
			File f3 = File.createTempFile("abcdefghijklmnopqrstuvwxyz123456789012345678901234567801234567890", "abc");
			assertFalse("long name without extension should fail", fv.validate(f3));			
			
			File f4 = File.createTempFile("def123457890_a-fr(de)", "abc.doc");
			assertTrue("name with valid chars should pass", fv.validate(f4));
			
			File f5 = File.createTempFile("abcd4567890^sdsadsfd", "abc");
			assertFalse("name with invalid chars should fail", fv.validate(f5));
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException:" + e.getMessage());			
		}		
	}

}
