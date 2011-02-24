package com.amdocs.filevalidator.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amdocs.filevalidator.config.ConfigBean;
import com.amdocs.filevalidator.config.ConfigManager;
import com.amdocs.filevalidator.modules.Module;
import com.amdocs.filevalidator.securityutilities.FileNameGenerator;
import com.amdocs.filevalidator.securityutilities.SizeBoundedInputStream;

/**
 * FUV implementation of the FileValidator
 * 
 * @author zach, rotem
 */
public class FileValidatorImpl implements FileValidator {

	/** Logger */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/** configuration bean */
	private static ConfigBean config = ConfigManager.getInstance().getConfigBean();
	
	
	/** Singleton */
	private static FileValidatorImpl inst;
	private FileValidatorImpl() {}
	public synchronized static FileValidatorImpl getInstance() {
		if (inst == null) { 
			inst = new FileValidatorImpl();
		}
		return inst;
	}
	
	
	public boolean validate(File file) throws IOException {
		
		logger.info("Validating file : {}", file.getAbsolutePath());
		
		InputStream is = new FileInputStream(file);
		
		try {
		
			// iterate all modules
			for (Module module : config.getModules()) {
				logger.debug("Validating module {}", module.getName());
				// TODO reset IS
				if (!module.validate(is, file.getAbsolutePath())) { 
					return false;
				}
			}	
			is.close();
			
			// check inner files
			is = new FileInputStream(file);
			return validateArchiveFiles(is, 0);
		
		
		} finally {
			is.close();
		}
	}
		


	/**
	 * 
	 * @param is
	 * @param depth
	 * @return
	 * @throws IOException
	 */
	private boolean validateArchiveFiles(InputStream is, int depth) throws IOException {

		// file has more levels than allowed
		if (depth > config.getArchiveRecDepth()){
			return false;
		}
		
		BufferedInputStream bis = new BufferedInputStream(is);		 		
		try {			
			ArchiveInputStream input = 
				new ArchiveStreamFactory().createArchiveInputStream(bis);
			
			if (input instanceof ZipArchiveInputStream) {
				// ZIP
				logger.info("Found ZIP file");				
				return isValidZipFile(depth, bis);	
				
			} else if (input instanceof TarArchiveInputStream) {
				// TAR
				logger.info("Found TAR file");				
				return isValidTarFile(depth, input);
				
			} else {
				logger.warn("AR/JAR/CPIO Archives files are not supported. Ignoring...");
			}			
		} catch (ArchiveException e) {
			// Not ZIP and not TAR
			logger.debug("Not ZIP/TAR");
			
			// check for BZIP
			bis.mark(4);
			try {
				return isValidBzipFile(depth, bis);							
			} catch (IOException ex) {
				logger.debug("Not BZIP");
				bis.reset();
			}			
			
			// check for GZIP
			bis.mark(4);
			try {
				return isValidGzipFile(depth, bis);					
			} catch (IOException ex) {
				logger.debug("Not GZIP");
				bis.reset();
			}
		}

		return true;
	}
	
	/**
	 * @param depth
	 * @param bis
	 * @return
	 * @throws IOException
	 */
	private boolean isValidGzipFile(int depth, BufferedInputStream bis)
			throws IOException {
		
		GzipCompressorInputStream gzIn = new GzipCompressorInputStream(bis);			
		logger.info("Found GZIP file");
		if (isValidInnerStream(gzIn, "")) { // TODO Gzip entry name?!
			// TODO gzIn.reset?
			return validateArchiveFiles(gzIn, depth+1);											
		} else {
			return false;
		}
	}
	
	/**
	 * @param depth
	 * @param bis
	 * @return
	 * @throws IOException
	 */
	private boolean isValidBzipFile(int depth, BufferedInputStream bis)
			throws IOException {
		
		BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(bis);		
		logger.info("Found BZIP file");
		if (isValidInnerStream(bzIn, "")) { // TODO BZIP entry name?!
			// TODO bzIn.reset?
			return validateArchiveFiles(bzIn, depth+1);											
		} else {
			return false;
		}
	}
	
	/**
	 * @param depth
	 * @param input
	 * @return
	 * @throws IOException
	 */
	private boolean isValidTarFile(int depth, ArchiveInputStream input)
			throws IOException {
		
		TarArchiveInputStream tarInput = (TarArchiveInputStream)input; 
		TarArchiveEntry entry = tarInput.getNextTarEntry();
		while (entry != null) {
			String name = entry.getName();
			// TODO check directory name?
			logger.info("Entry: " + name + (entry.isDirectory() ? "(directory). Skipping..." : ""));
			if (!entry.isDirectory()) { 
				SizeBoundedInputStream tis = new SizeBoundedInputStream(tarInput, entry.getSize());
				if (isValidInnerStream(tis, name)) {
					// TODO tis.reset?
					if (!validateArchiveFiles(tis, depth+1)){
						return false;
					}
				} else {
					return false;
				}
			
			}									
			entry = tarInput.getNextTarEntry();
		}
		return true;
	}
	
	/**
	 * @param depth
	 * @param bis
	 * @return
	 * @throws IOException
	 */
	private boolean isValidZipFile(int depth, BufferedInputStream bis) 
			throws IOException {
		
		ZipInputStream zis = new ZipInputStream(bis);
		
		ZipEntry entry = zis.getNextEntry();
		while (entry != null) {
			String name = entry.getName();
			// TODO check directory name?
			logger.info("Entry: " + name + (entry.isDirectory() ? "(directory). Skipping..." : ""));					
			if (!entry.isDirectory()) {				
				if (isValidInnerStream(zis, name)) {
					// TODO zis.reset?
					if (!validateArchiveFiles(zis, depth+1)){
						return false;
					}
				} else {
					return false;
				}						
			}					
			entry = zis.getNextEntry();
		}
		return true;
	}
	
	/**
	 * @param is	The entry input stream
	 * @param name	The entry name
	 */
	private boolean isValidInnerStream(InputStream is, String name) {

		// TODO reset IS
		
		// iterate all modules with scanInnerFiles flag
		for (Module module : config.getModules()) {
			if (module.scanInnerFiles()) {
				logger.debug("Validating module {}", module.getName());
				if (!module.validate(is, name)) { 
					return false;
				}
			}
		}
		return true;
	}
	
	
	public FileNameGenerator getFileNameGenerator(){
		return config.getFileNameGenerator();
	}

}
