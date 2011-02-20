package com.amdocs.filevalidator.utils;

import org.junit.Assert;
import org.junit.Test;

public class FileNameUtilsTest {

	
	@Test
	public void extractFileNameTest() { 
		Assert.assertEquals("file", FileNameUtils.extractFileName("file"));
		Assert.assertEquals("file", FileNameUtils.extractFileName("/file"));
		Assert.assertEquals("file", FileNameUtils.extractFileName("a/file"));
		Assert.assertEquals("file", FileNameUtils.extractFileName("a/b/c/file"));
		Assert.assertEquals("file", FileNameUtils.extractFileName("file/file"));
		Assert.assertEquals(null  , FileNameUtils.extractFileName("file/"));
		Assert.assertEquals(null  , FileNameUtils.extractFileName("file/file/"));
		Assert.assertEquals("file.ext", FileNameUtils.extractFileName("file.ext"));
		Assert.assertEquals("file.ext", FileNameUtils.extractFileName("dir/file.ext"));
		Assert.assertEquals("file.ext", FileNameUtils.extractFileName("dir.ext/file.ext"));
		Assert.assertEquals("file", FileNameUtils.extractFileName("dir.ext/file"));
		Assert.assertEquals("file.ext", FileNameUtils.extractFileName("/dir/dir2/file.ext"));
	}


	@Test
	public void extractFileExtensionTest() { 
		Assert.assertEquals("ext", FileNameUtils.extractFileExtension("file.ext"));
		Assert.assertEquals("ext2", FileNameUtils.extractFileExtension("a/b/file.ext2"));
		Assert.assertEquals("ext", FileNameUtils.extractFileExtension("file..ext"));
		Assert.assertEquals("ext", FileNameUtils.extractFileExtension("/tmp/file.ext"));
		Assert.assertEquals("ext2", FileNameUtils.extractFileExtension("file.ext.ext2"));
		Assert.assertEquals("ext", FileNameUtils.extractFileExtension("/home/users/user/file.ext"));
		Assert.assertEquals("ext", FileNameUtils.extractFileExtension("fi_le,-=.ext"));
		
	}
}
