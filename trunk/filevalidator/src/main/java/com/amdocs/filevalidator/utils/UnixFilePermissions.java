package com.amdocs.filevalidator.utils;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a unix file perrmissions.
 * can be built explicitly or from a File object and supports compare() method.
 * 
 * @author zach, rotem
 */
public class UnixFilePermissions implements Comparable<UnixFilePermissions> {

	private short user;
	private short group;
	private short all;
	
	public UnixFilePermissions(int user, int group, int all) { 
		this.user = (short)user;
		this.group = (short)group;
		this.all = (short)all;
	}

	/**
	 * Built from the unix permissions representation ("rw-", "r--", ...)
	 */
	public UnixFilePermissions(String userPerm, String groupPerm, String allPerm) { 
		if (userPerm==null || userPerm.length()!=3 || groupPerm==null || groupPerm.length()!=3 || allPerm==null || allPerm.length()!=3) { 
			throw new IllegalArgumentException("all 3 perms must be not null and of length 3");
		}
		
		this.user = parseUnixPermissionsString(userPerm);
		this.group = parseUnixPermissionsString(groupPerm);
		this.all = parseUnixPermissionsString(allPerm);
	}
	
	private short parseUnixPermissionsString(String str) { 
		short perm = 0;
		if (str.charAt(0)!='-') perm += 4;
		if (str.charAt(1)!='-') perm += 2;
		if (str.charAt(2)!='-') perm += 1;
		return perm;
	}
	
	private String parseUnixPermissionsNumber(short num) { 
		StringBuilder sb = new StringBuilder();
		sb.append((num & 4)>0 ? "r" : "-");
		sb.append((num & 2)>0 ? "w" : "-");
		sb.append((num & 1)>0 ? "x" : "-");
		return sb.toString();
	}
	
	/**
	 * Built from a File object. Uses 'ls' to find the permissions
	 */
	private Pattern PERMS_STR_PATTERN = Pattern.compile("^([-drwx]{10})\\s.*$");
	public UnixFilePermissions(File file) { 
		
		if (!file.isFile() || !file.canRead()) { 
			throw new IllegalArgumentException(file.getAbsolutePath() + " is not a file or not accessible");
		}
		
		try {
			Process ps = Runtime.getRuntime().exec("/bin/ls -l " + file.getAbsolutePath());
			ps.waitFor();
			
			if (ps.exitValue() != 0) { 
				handleError(ps);
			}
			
			Scanner s = new Scanner(ps.getInputStream());
			String firstLine = s.nextLine();
			Matcher m = PERMS_STR_PATTERN.matcher(firstLine);
			if (m.matches()) {
				String allPerm = m.group(1);
				this.user = parseUnixPermissionsString(allPerm.substring(1, 4));
				this.group = parseUnixPermissionsString(allPerm.substring(4,7));
				this.all = parseUnixPermissionsString(allPerm.substring(7,10));
			} else { 
				throw new RuntimeException("Illegal output from 'ls' command : " + firstLine);
			}
			
			
		} catch (Exception e) {
			throw new RuntimeException("Error while running ls : " + e.getMessage(), e);
		} 
			
	}
	
	
	private void handleError(Process ps) { 
		Scanner s = new Scanner(ps.getErrorStream());
		StringBuilder sb = new StringBuilder();
		
		while (s.hasNextLine()) {
			sb.append(s.nextLine());
		}
		throw new RuntimeException("Error from /bin/ls : " + sb.toString());
	}

	@Override
	public String toString() {
		return parseUnixPermissionsNumber(this.user) + "," + parseUnixPermissionsNumber(this.group) + "," + parseUnixPermissionsNumber(this.all);
	}
	
	/**
	 * Returns -1 if the given object doesn't contain a permission that the current object doesn't have.
	 * 0 if they are equal,
	 * and 1 if there is at least one permission in the given object that is not allowed in the current object, 
	 */
	@Override
	public int compareTo(UnixFilePermissions o) {
		if (this.user == o.user && this.group == o.group && this.all == o.all) { 
			return 0;
		}
		
		if (stronglyStrict(this.user, o.user) || stronglyStrict(this.group, o.group) || stronglyStrict(this.all, o.all)) { 
			return 1;
		}
		
		return -1;
	}

	/** 
	 * returns true if perm2 has at least one permission that perm1 doesn't have
	 */
	private static boolean stronglyStrict(short perm1, short perm2) { 
		return (~perm1 & perm2) > 0;
	}
	
	
}
