package com.amdocs.filevalidator.core;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

public class FileValidatorImplTester {

	private static final String MULTIPLE_ZIP_TEST_FILE = "src/test/resources/tar_with_dir.tar.gz";
	private static final String XML_TEST_FILE = "src/test/resources/example.xml";
	private static final String TEXT_ZIP_TEST_FILE = "src/test/resources/textfile.gz";
	private static final String TEXT_ZIP_NOEXT_TEST_FILE = "src/test/resources/textfile_noext.tar";
	private static final String TAR_7_LEVELS_TEST_FILE = "src/test/resources/text_7_levels_tar.tar";
	
	
	
	@Test
	public void test() throws Exception  { 
		FileValidator fv = FileValidatorImpl.getInstance();
		Assert.assertFalse(fv.validate(new File(MULTIPLE_ZIP_TEST_FILE)));
		Assert.assertFalse(fv.validate(new File(XML_TEST_FILE)));
		Assert.assertTrue(fv.validate(new File(TEXT_ZIP_TEST_FILE)));
		Assert.assertFalse(fv.validate(new File(TEXT_ZIP_NOEXT_TEST_FILE)));
		Assert.assertFalse(fv.validate(new File(TAR_7_LEVELS_TEST_FILE)));
	}
	
	
}
