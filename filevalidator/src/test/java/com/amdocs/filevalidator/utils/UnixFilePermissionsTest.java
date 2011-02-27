package com.amdocs.filevalidator.utils;

import junit.framework.Assert;

import org.junit.Test;

public class UnixFilePermissionsTest {

	@Test
	public void testEqualsPermissions() { 
		Assert.assertEquals(0, new UnixFilePermissions(0,0,0).compareTo(new UnixFilePermissions(0,0,0)));
		Assert.assertEquals(0, new UnixFilePermissions(1,0,0).compareTo(new UnixFilePermissions(1,0,0)));
		Assert.assertEquals(0, new UnixFilePermissions(7,0,0).compareTo(new UnixFilePermissions(7,0,0)));
		Assert.assertEquals(0, new UnixFilePermissions(0,4,0).compareTo(new UnixFilePermissions(0,4,0)));
		Assert.assertEquals(0, new UnixFilePermissions(0,0,3).compareTo(new UnixFilePermissions(0,0,3)));
		Assert.assertEquals(0, new UnixFilePermissions(0,3,3).compareTo(new UnixFilePermissions(0,3,3)));
		Assert.assertEquals(0, new UnixFilePermissions(7,7,0).compareTo(new UnixFilePermissions(7,7,0)));
		Assert.assertEquals(0, new UnixFilePermissions(7,5,5).compareTo(new UnixFilePermissions(7,5,5)));
		Assert.assertEquals(0, new UnixFilePermissions(7,0,7).compareTo(new UnixFilePermissions(7,0,7)));
	}
	
	@Test
	public void testSecondAllowsMore() { 
		UnixFilePermissions noPerms = new UnixFilePermissions(0,0,0);
		for (int i=0 ; i<8 ; i++) { 
			for (int j=0 ; j<8 ; j++) { 
				for (int k=0 ; k<8 ; k++) {
					if (i==0 && j==0 && k==0) continue;
					Assert.assertEquals(1, noPerms.compareTo(new UnixFilePermissions(i,j,k)));
				}
			}
		}
		Assert.assertEquals(1, new UnixFilePermissions(1,0,0).compareTo(new UnixFilePermissions(1,0,1)));
		Assert.assertEquals(1, new UnixFilePermissions(1,1,0).compareTo(new UnixFilePermissions(1,0,1)));
		Assert.assertEquals(1, new UnixFilePermissions(1,1,1).compareTo(new UnixFilePermissions(1,1,4)));
		Assert.assertEquals(1, new UnixFilePermissions(5,5,5).compareTo(new UnixFilePermissions(1,2,1)));
		Assert.assertEquals(1, new UnixFilePermissions(5,0,0).compareTo(new UnixFilePermissions(2,0,0)));
		Assert.assertEquals(1, new UnixFilePermissions(7,6,7).compareTo(new UnixFilePermissions(0,1,0)));
		Assert.assertEquals(1, new UnixFilePermissions(4,5,4).compareTo(new UnixFilePermissions(0,3,0)));
		Assert.assertEquals(1, new UnixFilePermissions(4,5,4).compareTo(new UnixFilePermissions(1,0,0)));
		Assert.assertEquals(1, new UnixFilePermissions(6,6,0).compareTo(new UnixFilePermissions(1,0,0)));
	}


	@Test
	public void testFirstAllowsMore() { 
		UnixFilePermissions noPerms = new UnixFilePermissions(7,7,7);
		for (int i=0 ; i<8 ; i++) { 
			for (int j=0 ; j<8 ; j++) { 
				for (int k=0 ; k<8 ; k++) {
					if (i==7 && j==7 && k==7) continue;
					Assert.assertEquals(-1, noPerms.compareTo(new UnixFilePermissions(i,j,k)));
				}
			}
		}
		Assert.assertEquals(-1, new UnixFilePermissions(3,0,0).compareTo(new UnixFilePermissions(1,0,0)));
		Assert.assertEquals(-1, new UnixFilePermissions(7,5,0).compareTo(new UnixFilePermissions(1,4,0)));
		Assert.assertEquals(-1, new UnixFilePermissions(6,6,0).compareTo(new UnixFilePermissions(4,4,0)));
		Assert.assertEquals(-1, new UnixFilePermissions(7,7,1).compareTo(new UnixFilePermissions(7,7,0)));
		Assert.assertEquals(-1, new UnixFilePermissions(5,5,5).compareTo(new UnixFilePermissions(1,4,4)));
	}
	
	
}
