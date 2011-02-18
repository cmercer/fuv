package com.amdocs.filevalidator.securityutilities;

import java.io.IOException;
import java.io.InputStream;

import com.amdocs.filevalidator.config.ConfigManager;
import com.amdocs.filevalidator.exceptions.FileSizeException;

public class SizeBoundedInputStream extends InputStream {
	
	private InputStream originalIunputStream;
	
	private long maxSize;
	private long size = 0;
	
	public SizeBoundedInputStream(InputStream is) {
		originalIunputStream = is;		
		maxSize = ConfigManager.getInstance().getConfigBean().getMaxFileSize();
	}

	@Override
	public int read() throws IOException {
		
		size++;
		if (size > maxSize) 
			throw new FileSizeException("Maximum file size allowed: " + maxSize);
				
		return originalIunputStream.read();
	}

	@Override
    public int available() throws IOException {
    	return originalIunputStream.available();
    }
	
	@Override
	public void close() throws IOException {
		originalIunputStream.close();
	}
}
