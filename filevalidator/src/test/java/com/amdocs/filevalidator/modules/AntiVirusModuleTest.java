package com.amdocs.filevalidator.modules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for AntiVirus Module
 * @author zach, rotem
 *
 */
public class AntiVirusModuleTest  {
	
	
	protected static AntiVirusModule module;
	
	private static final String EICAR_FILE_CONTENT = "X5O!P%@AP[4\\PZX54(P^)7CC)7}$EICAR-STANDARD-ANTIVIRUS-TEST-FILE!$H+H*";
	
	@BeforeClass
	public static void setUp() throws Exception {
		
		JAXBContext jc = JAXBContext.newInstance(AntiVirusModule.class);
		Unmarshaller um = jc.createUnmarshaller();
		
		module = (AntiVirusModule)um.unmarshal(new StringReader("<anti-virus-module scanInnerFiles=\"false\"><anti-virus-path>bin/av_wrapper.sh</anti-virus-path><success-rc>0</success-rc></anti-virus-module>"));
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
			assertTrue("File shouldn't contain viruses", module.validate(f, false));
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
			assertFalse("File should contain a virus", module.validate(f, false));
		} catch (IOException e) {
			if (f != null && f.exists()) f.delete();
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	
}
