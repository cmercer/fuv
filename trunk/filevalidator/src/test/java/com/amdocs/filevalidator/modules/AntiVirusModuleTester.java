package com.amdocs.filevalidator.modules;

import java.io.File;
import java.io.FileWriter;
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
	
	private static final String EICAR_FILE_CONTENT = "X5O!P%@AP[4\\PZX54(P^)7CC)7}$EICAR-STANDARD-ANTIVIRUS-TEST-FILE!$H+H*";
	
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
	public void testValidateNoVirus() {
		
		File f = null;
		try {
			f = File.createTempFile("av-tester", null);
			FileWriter fw = new FileWriter(f);
			fw.write("NO VIRUS FOR YOU !");
			fw.close();
			assertTrue("File shouldn't contain viruses", fv.validate(f));
		} catch (IOException e) {
			if (f != null && f.exists()) f.delete();
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	@Test
	public void testValidateEicarVirus() {
		
		File f = null;
		try {
			f = File.createTempFile("av-tester", null);
			FileWriter fw = new FileWriter(f);
			fw.write(EICAR_FILE_CONTENT);
			fw.close();
			assertFalse("File should contain a virus", fv.validate(f));
		} catch (IOException e) {
			if (f != null && f.exists()) f.delete();
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	
}
