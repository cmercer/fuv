package com.amdocs.filevalidator.utils;

import org.junit.Assert;
import org.junit.Test;

public class FileNameUtilsTest {



	@Test
	public void extractFileExtensionTest() { 
		Assert.assertEquals("", FileNameUtils.extractFileExtension("file"));
		Assert.assertEquals("", FileNameUtils.extractFileExtension("/dir/file"));
		Assert.assertEquals("ext", FileNameUtils.extractFileExtension("file.ext"));
		Assert.assertEquals("ext2", FileNameUtils.extractFileExtension("a/b/file.ext2"));
		Assert.assertEquals("ext", FileNameUtils.extractFileExtension("file..ext"));
		Assert.assertEquals("ext", FileNameUtils.extractFileExtension("/tmp/file.ext"));
		Assert.assertEquals("ext2", FileNameUtils.extractFileExtension("file.ext.ext2"));
		Assert.assertEquals("ext", FileNameUtils.extractFileExtension("/home/users/user/file.ext"));
		Assert.assertEquals("ext", FileNameUtils.extractFileExtension("fi_le,-=.ext"));
	}
	
	
	@Test
	public void removeExtensionTest() { 
		Assert.assertEquals("", FileNameUtils.removeExtension(""));
		Assert.assertEquals("file", FileNameUtils.removeExtension("file"));
		Assert.assertEquals("file", FileNameUtils.removeExtension("file.ext"));
		Assert.assertEquals("/dir/file", FileNameUtils.removeExtension("/dir/file.ext"));
		Assert.assertEquals("", FileNameUtils.removeExtension(".doc"));
		Assert.assertEquals("file", FileNameUtils.removeExtension("file."));
		Assert.assertEquals("file.ext1", FileNameUtils.removeExtension("file.ext1.ext2"));
		Assert.assertEquals("/tmp/a/b/c/file1.ex1", FileNameUtils.removeExtension("/tmp/a/b/c/file1.ex1.ex2"));
		Assert.assertEquals("file_!@#$%", FileNameUtils.removeExtension("file_!@#$%.ext"));
		Assert.assertEquals("@#%file@#$%", FileNameUtils.removeExtension("@#%file@#$%"));
		
	}
	
}
