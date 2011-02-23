package com.amdocs.filevalidator.securityutilities;

import java.io.IOException;
import java.io.InputStream;

import com.amdocs.filevalidator.config.ConfigManager;

/**
 * Input stream for file size validation.
 * If the size is bigger than maximum allowed (in configuration) - exception is thrown
 * @author zach, rotem
 *
 */
public class SizeBoundedInputStream extends InputStream {
	
	/** The original input stream */
	private InputStream originalIunputStream;
	
	/** The maximum file from configuration */
	private long maxSize;
	
	/* the current size of the stream */
	private long size = 0;
	
	/** Have we reached the maxSize limit ? */
	private boolean limitReached = false;
	
	public SizeBoundedInputStream(InputStream is) {
		originalIunputStream = is;		
		maxSize = ConfigManager.getInstance().getConfigBean().getMaxFileSize();
	}

	@Override
	public int read() throws IOException {
		size++;
		if (size > maxSize) {
			this.limitReached = true;
			return -1;
		}
				
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
	
	/**
	 * Returns true if the input stream has reached the limit 
	 */
	public boolean hasReachedLimit() {
		return this.limitReached;
	}
	
}
