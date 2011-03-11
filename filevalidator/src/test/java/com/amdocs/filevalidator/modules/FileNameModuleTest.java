package com.amdocs.filevalidator.modules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.StringReader;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amdocs.filevalidator.config.CharStrip;

/**
 * Tests for the file name module
 * @author zach, rotem
 *
 */
public class FileNameModuleTest {
	
	private static String tempDir = System.getProperty("java.io.tmpdir");
	
	private static FileNameModule moduleMax50AllowedDCO;
	private static String MAX_50_ALLOWED_DCO_STR = "<file-name-module><max-file-name-length>50</max-file-name-length><allowedCharStrips>D C O</allowedCharStrips></file-name-module>";
	private static FileNameModule moduleMax5AllowedDCO;
	private static String MAX_5_ALLOWED_DCO_STR = "<file-name-module><max-file-name-length>5</max-file-name-length><allowedCharStrips>D C O</allowedCharStrips></file-name-module>";
	private static FileNameModule moduleMax50AllowedD;
	private static String MAX_50_ALLOWED_D_STR = "<file-name-module><max-file-name-length>50</max-file-name-length><allowedCharStrips>D</allowedCharStrips></file-name-module>";
	
	
	@BeforeClass
	public static void init() throws Exception {
		CharStrip stripD = new CharStrip("D", "0123456789");
		CharStrip stripC = new CharStrip("C", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
		CharStrip stripO = new CharStrip("O", "_-)(.");
		
		JAXBContext jc = JAXBContext.newInstance(FileNameModule.class);
		Unmarshaller um = jc.createUnmarshaller();
		
		moduleMax50AllowedDCO = (FileNameModule)um.unmarshal(new StringReader(MAX_50_ALLOWED_DCO_STR));
		moduleMax5AllowedDCO = (FileNameModule)um.unmarshal(new StringReader(MAX_5_ALLOWED_DCO_STR));
		moduleMax50AllowedD = (FileNameModule)um.unmarshal(new StringReader(MAX_50_ALLOWED_D_STR));
		
		// inject the strips
		moduleMax50AllowedDCO.getAllowedCharStrips().addAll(Arrays.asList(stripD, stripC, stripO));
		moduleMax5AllowedDCO.getAllowedCharStrips().addAll(Arrays.asList(stripD, stripC, stripO));
		moduleMax50AllowedD.getAllowedCharStrips().add(stripD);
		
		
	}

	@Test
	public void testScanInnerFiles() {		
		assertTrue("Module should check inner files", moduleMax50AllowedDCO.scanInnerFiles()); // default value
		assertTrue("Module should check inner files", moduleMax5AllowedDCO.scanInnerFiles()); // default value
		assertTrue("Module should check inner files", moduleMax50AllowedD.scanInnerFiles()); // default value
	}

	@Test
	public void testModuleMax50AllowedDCO() throws Exception {

		assertTrue("short name without extension should pass", moduleMax50AllowedDCO.validate(new File(tempDir,"abc.abc"), false));			
		assertTrue("short name with extension should pass", moduleMax50AllowedDCO.validate(new File(tempDir,"def.abc.doc"), false));
		assertFalse("long name without extension should fail", moduleMax50AllowedDCO.validate(new File(tempDir,"abcdefghijklmnopqrstuvwxyz123456789012345678901234567801234567890"), false));			
		assertTrue("name with valid chars should pass", moduleMax50AllowedDCO.validate(new File(tempDir,"def123457890_a-fr(de).doc"), false));
		assertFalse("name with invalid chars should fail", moduleMax50AllowedDCO.validate(new File(tempDir,"abcd4567890^sdsadsfd.dbc"), false));
	}
	
	@Test
	public void testModuleMax5AllowedDCO() throws Exception { 
		assertTrue("short name without extension should pass", moduleMax5AllowedDCO.validate(new File(tempDir,"abc.abc"), false));			
		assertTrue("short name with extension should pass", moduleMax5AllowedDCO.validate(new File(tempDir,"d.a.doc"), false));
		assertFalse("long name without extension should fail", moduleMax5AllowedDCO.validate(new File(tempDir,"abcdefghijklmnopqrstuvwxyz123456789012345678901234567801234567890"), false));			
		assertFalse("name with valid chars should fail (too long)", moduleMax5AllowedDCO.validate(new File(tempDir,"def123457890_a-fr(de).doc"), false));
		assertFalse("name with invalid chars should fail", moduleMax5AllowedDCO.validate(new File(tempDir,"abcd4567890^sdsadsfd.abc"), false));
	}
	
	@Test
	public void testModuleMax50AllowedD() throws Exception {
		assertTrue("short name without extension should pass", moduleMax50AllowedD.validate(new File(tempDir,"123"), false));			
		assertTrue("short name with extension should pass", moduleMax50AllowedD.validate(new File(tempDir,"123.abc"), false));
		assertFalse("contains chracters", moduleMax50AllowedD.validate(new File(tempDir,"def.abc.doc"), false));
		assertFalse("double extention. should remove only the 2'nd ext", moduleMax50AllowedD.validate(new File(tempDir,"123.abc.doc"), false));
		assertFalse("long name (chars) should fail", moduleMax50AllowedD.validate(new File(tempDir,"abcdefghijklmnopqrstuvwxyz123456789012345678901234567801234567890"), false));		
		assertFalse("long name (digits) should fail", moduleMax50AllowedD.validate(new File(tempDir,"123456789012345678901234567801234567890123456789012345678901234567801234567890"), false));
		assertFalse("name with invalid chars should fail", moduleMax50AllowedD.validate(new File(tempDir,"a4567890.ext"), false));
	}

}
