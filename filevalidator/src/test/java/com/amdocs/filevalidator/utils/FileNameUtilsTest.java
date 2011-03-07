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
}
