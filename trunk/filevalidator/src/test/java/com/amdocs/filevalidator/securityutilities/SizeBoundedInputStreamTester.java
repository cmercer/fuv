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
public class SizeBoundedInputStreamTester extends TestCase {

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
			System.out.println("File size:" + f1.length());

			// valid file
			SizeBoundedInputStream is = new SizeBoundedInputStream(new FileInputStream(f1));
			int c;
			int count = 0;
			while ((c=is.read()) >=0) {
				count++;
				System.out.print((char)c);
			}
			assertFalse(is.hasReachedLimit());
			is.close();
			System.out.println();
			System.out.println("SizeBoundedInputStream. File size:" + count);


			// valid file with buffered input stream
			is = new SizeBoundedInputStream(new FileInputStream(f1));
			BufferedInputStream is2 = new BufferedInputStream(is);
			count = 0;
			while ((c=is2.read()) >=0) {
				System.out.print((char)c);
				count++;
			}
			assertFalse(is.hasReachedLimit());
			is2.close();
			System.out.println();
			System.out.println("BufferedInputStream. File size:" + count);

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
			int c;
			SizeBoundedInputStream is = null;
			BufferedInputStream is2 = null;
			
			f1 = File.createTempFile("abc", "abc");		
			FileWriter writer = new FileWriter(f1);				
			writer.append("abcdefghijklmnopqrstuvwxyz");
			writer.close();
			System.out.println("File size:" + f1.length());


			// big file. Exception is thrown after 15 chars
			is = new SizeBoundedInputStream(new FileInputStream(f1));
			count = 0;			
			while ((c=is.read()) >=0) {
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
			while ((c=is2.read()) >=0) {	
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
