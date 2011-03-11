package com.amdocs.filevalidator.securityutilities;


import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.amdocs.filevalidator.core.FileValidator;
import com.amdocs.filevalidator.core.FileValidatorImpl;
import com.amdocs.filevalidator.exceptions.FilenameGenerationException;

public class FileNameGeneratorTest extends TestCase{
	
	protected FileValidator fv;

	@Before
	public void setUp() throws Exception {
		fv = FileValidatorImpl.getInstance();
	}

	@Test
	public void testGenerateNewRandomFilename() {
		
		FileNameGenerator gen = fv.getFileNameGenerator();
		
		try {
			String name1 = gen.generateNewRandomFilename();
			String name2 = gen.generateNewRandomFilename();
		
			assertFalse("random name failed", name1.equals(name2));
		
			assertTrue("Generate file according to pattern: " + name1, name1.matches("\\w{10}\\d{2}"));
			assertTrue("Generate file according to pattern: " + name2, name2.matches("\\w{10}\\d{2}"));
		
		} catch (FilenameGenerationException e) {
			fail("Problem with configuration");
		}
	}

	@Test
	public void testCensorFilename() {
		
		FileNameGenerator gen = fv.getFileNameGenerator();
		try {
			String name1 = gen.censorFilename("sf3ds_4_sdfsf_t54tfgvsfjsdkfjeshdfgejhgrfejwgdjawhsdakfdhekfhae.txt.doc");
			assertTrue("fit name to size", name1.length() == 30);
			assertTrue("keep extension", name1.endsWith(".doc"));
			assertFalse("remove invalid chars", name1.contains("5"));
		
			String name2 = gen.censorFilename("sf3ddfsf_t54hae.txt.doc");
			assertTrue("fit name to size", name2.length() < 30);
		
		} catch (FilenameGenerationException e) {
			fail("Problem with configuration");
		}
		
				
		try {
		
			gen.censorFilename("345678.doc");
			fail("empty file name");
		
		} catch (FilenameGenerationException e) {
			assertTrue(true);
		}
	}

}
