package com.amdocs.filevalidator.securityutilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for SizeBoundedInputStream
 * @author zach, rotem
 *
 */
public class SizeBoundedInputStreamTest extends TestCase {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testRead() {

		File f1 = null;

		try {
			f1 = File.createTempFile("abc", "abc");

			FileWriter writer = new FileWriter(f1);
			writer.append("abcdefghij");		
			writer.close();

			// valid file
			SizeBoundedInputStream is = new SizeBoundedInputStream(new FileInputStream(f1));
			int count = 0;
			while (is.read() >=0) {
				count++;
			}
			assertFalse(is.hasReachedLimit());
			is.close();


			// valid file with buffered input stream
			is = new SizeBoundedInputStream(new FileInputStream(f1));
			BufferedInputStream is2 = new BufferedInputStream(is);
			count = 0;
			while (is2.read() >=0) {
				count++;
			}
			assertFalse(is.hasReachedLimit());
			is2.close();

			f1.delete();

		} catch (Exception e) {			
			if (f1.exists()) f1.delete();
			fail("Exception: " + e.getMessage());						
		}

	}


	@Test
	public void testRead2() {

		File f1 = null;


		try {
			
			int count;
			SizeBoundedInputStream is = null;
			BufferedInputStream is2 = null;
			
			f1 = File.createTempFile("abc", "abc");		
			FileWriter writer = new FileWriter(f1);				
			writer.append("abcdefghijklmnopqrstuvwxyz");
			writer.close();


			// big file. Exception is thrown after 15 chars
			is = new SizeBoundedInputStream(new FileInputStream(f1));
			count = 0;			
			while (is.read() >=0) {
				count++;
			}						
			assertEquals(count, 15);
			assertTrue(is.hasReachedLimit());
			is.close();	


			// big file with buffered input stream. 
			// Exception is thrown in the first read (the buffer is bigger than 15 chars)
			is = new SizeBoundedInputStream(new FileInputStream(f1));
			is2 = new BufferedInputStream(is);
			count = 0;			
			while (is2.read() >=0) {	
				count++;
			}
			assertEquals(count, 15);
			assertTrue(is.hasReachedLimit());
			is2.close();				

			f1.delete();
			
		} catch (Exception e) {
			if (f1.exists()) f1.delete();
			fail("Exception: " + e.getMessage());
		}
	}
}
