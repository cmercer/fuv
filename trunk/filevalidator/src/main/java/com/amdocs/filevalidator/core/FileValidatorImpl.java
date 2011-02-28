package com.amdocs.filevalidator.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
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

	private static String tmpDir = System.getProperty("java.io.tmpdir");
	private Random rand = new Random(); 


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

		// iterate all modules

		for (Module module : config.getModules()) {
			logger.debug("Validating module {}", module.getName());
			if (!module.validate(file.getAbsolutePath(), file.getName())) { 
				return false;
			}
		}	


		// check inner files

		String temporaryDirName = tmpDir + File.separator + "FUV" + rand.nextInt();
		File temporaryDir = new File(temporaryDirName); 
		temporaryDir.mkdir();		
		boolean result;
		try {
			result = validateArchiveFiles(file, 0, temporaryDir);
		} finally {		
			deleteDir(temporaryDir);
		}		
		return result;
	}



	/**
	 * Validate inner files
	 * @param file	the file to check
	 * @param depth level inside file
	 * @param temporaryDir temporary directory (keep the open archive files
	 * @return true if file is valid
	 * @throws IOException
	 */
	private boolean validateArchiveFiles(File file, int depth, File temporaryDir) 
	throws IOException {

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));		 		
		try {			
			ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(bis);

			if (input instanceof ZipArchiveInputStream) {

				// ZIP

				logger.info("Found ZIP file");

				// file has more levels than allowed
				if (depth >= config.getArchiveRecDepth()){
					logger.warn("file has more levels (" + (depth+1) + ") than allowed (" + config.getArchiveRecDepth() + ")");
					input.close();
					return false;
				}

				// using java.util.zip instead of org.apache.commons.compress
				input.close();
				bis = new BufferedInputStream(new FileInputStream(file));

				return isValidZipFile(depth, bis, temporaryDir);	

			} else if (input instanceof TarArchiveInputStream) {

				// TAR

				logger.info("Found TAR file");	

				// file has more levels than allowed
				if (depth >= config.getArchiveRecDepth()){
					logger.warn("file has more levels (" + (depth+1) + ") than allowed (" + config.getArchiveRecDepth() + ")");
					input.close();
					return false;
				}

				return isValidTarFile(depth, (TarArchiveInputStream)input, temporaryDir);

			} else {
				logger.warn("AR/JAR/CPIO Archives files are not supported. Ignoring...");
				input.close();
				return true;
			}			
		} catch (ArchiveException e) {
			// Not ZIP and not TAR
			logger.debug("Not ZIP/TAR/AR/JAR/CPIO... Going to check if BZIP2/GZIP");

			// check for BZIP

			bis.mark(4);
			BZip2CompressorInputStream bzIn = null;
			try {
				bzIn = new BZip2CompressorInputStream(bis);																	
			} catch (IOException ex) {
				logger.debug("Not BZIP2");
				bis.reset();
			} 
			if (bzIn != null) {
				logger.info("Found BZIP2 file");

				// file has more levels than allowed
				if (depth >= config.getArchiveRecDepth()){
					logger.warn("file has more levels (" + (depth+1) + ") than allowed (" + config.getArchiveRecDepth() + ")");
					bzIn.close();
					return false;
				}

				return isValidBzipFile(depth, bzIn, temporaryDir);
			}

			// check for GZIP

			bis.mark(4);
			GzipCompressorInputStream gzIn = null;
			try {
				gzIn = new GzipCompressorInputStream(bis);											
			} catch (IOException ex) {
				logger.debug("Not GZIP");
				bis.reset();
			}
			if (gzIn != null) {
				logger.info("Found GZIP file");

				// file has more levels than allowed
				if (depth >= config.getArchiveRecDepth()){
					logger.warn("file has more levels (" + (depth+1) + ") than allowed (" + config.getArchiveRecDepth() + ")");
					gzIn.close();
					return false;
				}


				return isValidGzipFile(depth, gzIn, temporaryDir);	
			}
		}
		
		bis.close();
		return true;
	}

	/**
	 * Validate GZIP file
	 * @param depth the level inside the file
	 * @param gzIn GZIP file to check
	 * @param temporaryDir Directory for temporary files 
	 * @return true if file is valid
	 * @throws IOException
	 */
	private boolean isValidGzipFile(int depth, GzipCompressorInputStream gzIn,
			File temporaryDir) throws IOException {

		// create temporary file	
		String tmpFileName = createTempFile(temporaryDir, gzIn, "tmpGzip");  // TODO Gzip entry name?!
		gzIn.close();

		// check temporary file
		File tempFile = new File(tmpFileName);		
		if (isValidInnerFile(tempFile)) {
			return validateArchiveFiles(tempFile, depth+1, temporaryDir);											
		} else {
			return false;
		}		
	}

	/**
	 * Validate BZIP2 file
	 * @param depth the level inside the file
	 * @param bzIn BZIP2 file to check
	 * @param temporaryDir Directory for temporary files 
	 * @return true if file is valid
	 * @throws IOException
	 */
	private boolean isValidBzipFile(int depth, BZip2CompressorInputStream bzIn, 
			File temporaryDir) throws IOException {
		
		// create temporary file	
		String tmpFileName = createTempFile(temporaryDir, bzIn, "tmpBzip");  // TODO BZIP entry name?!
		bzIn.close();

		// check temporary file
		File tempFile = new File(tmpFileName);	
		if (isValidInnerFile(tempFile)) {	 
			return validateArchiveFiles(tempFile, depth+1, temporaryDir);											
		} else {
			return false;
		}
	}

	/**
	 * Validate TAR file
	 * @param depth the level inside the file
	 * @param tarInput TAR file to check
	 * @param temporaryDir Directory for temporary files 
	 * @return true if file is valid
	 * @throws IOException
	 */
	private boolean isValidTarFile(int depth, TarArchiveInputStream tarInput, 
			File temporaryDir) throws IOException {

		try {

			TarArchiveEntry entry = tarInput.getNextTarEntry();
			while (entry != null) {
				String name = entry.getName();
				logger.info("Entry: " + name + 
						(entry.isDirectory() ? "(directory). Skipping..." : ""));
				
				if (entry.isDirectory()) {

					(new File(temporaryDir + File.separator + name)).mkdirs(); // TODO check directory name?

				} else {
					SizeBoundedInputStream tis = 
						new SizeBoundedInputStream(tarInput, entry.getSize());				

					// create temporary file	
					String tmpFileName = createTempFile(temporaryDir, tis, name);				

					// check temporary file
					File tempFile = new File(tmpFileName);					
					if (isValidInnerFile(tempFile)) {
						if (!validateArchiveFiles(tempFile, depth+1, temporaryDir)){
							return false;
						}
					} else {
						return false;
					}

				}									
				entry = tarInput.getNextTarEntry();
			}

		} finally {
			try {
				if (tarInput != null) tarInput.close();				
			} catch (IOException e) {
				logger.warn("Cannot close input stream", e);
			}
		}

		return true;
	}

	/**
	 * Validate ZIP file
	 * @param depth the level inside the file
	 * @param bis ZIP file to check
	 * @param temporaryDir Directory for temporary files 
	 * @return true if file is valid
	 * @throws IOException
	 */
	private boolean isValidZipFile(int depth, BufferedInputStream bis, 
			File temporaryDir) throws IOException {

		ZipInputStream zis = null;

		try {
			zis = new ZipInputStream(bis);

			ZipEntry entry = zis.getNextEntry();
			while (entry != null) {
				String name = entry.getName();			
				logger.info("Entry: " + name + (entry.isDirectory() ? "(directory). Skipping..." : ""));

				if (entry.isDirectory()) { 

					(new File(temporaryDir + File.separator + name)).mkdirs(); // TODO check directory name?

				} else {

					// create temporary file				
					String tmpFileName = createTempFile(temporaryDir, zis, name);

					// check temporary file
					File tempFile = new File(tmpFileName);
					if (isValidInnerFile(tempFile)) {					
						if (!validateArchiveFiles(tempFile, depth+1, temporaryDir)){						
							return false;
						}
					} else {
						return false;
					}						
				}					

				entry = zis.getNextEntry();
			}

		} finally {
			try {
				if (zis != null) {
					zis.close();
				} else if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				logger.warn("Cannot close input stream", e);
			}
		}

		return true;
	}
	
	/**
	 * Create temporary file from source input stream in the temporary directory
	 * @param temporaryDir destination directory
	 * @param is source file
	 * @param name simple name of the target file
	 * @return full name and path of the target file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String createTempFile(File temporaryDir, InputStream is,
			String name) throws FileNotFoundException, IOException {

		String tmpFileName = temporaryDir + File.separator + name;

		logger.debug("creating temporary file: " + tmpFileName);		
		OutputStream out = new BufferedOutputStream(new FileOutputStream(tmpFileName));

		copyInputStream(is, out);
		out.close();

		return tmpFileName;
	}

	/**
	 * @param tempFile the file to check
	 */
	private boolean isValidInnerFile(File tempFile) {

		// iterate all modules with scanInnerFiles flag
		for (Module module : config.getModules()) {
			if (module.scanInnerFiles()) {
				logger.debug("Validating module {}", module.getName());
				if (!module.validate(tempFile.getAbsolutePath(), tempFile.getName())) { 
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Copy data from input stream to output stream
	 * @param in source	
	 * @param out target
	 * @throws IOException
	 */
	private static void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);
	}

	/**
	 * Deletes all files and sub directories under directory.
	 * @param dir The directory to delete
	 * @return true if all deletions were successful
	 */
	private static boolean deleteDir(File dir) {    
		if (dir.isDirectory()) {        
			String[] children = dir.list();        
			for (int i=0; i<children.length; i++) {            
				boolean success = deleteDir(new File(dir, children[i]));			           
				if (!success) {                
					return false;            
				}        
			}    
		}    
		// The directory is now empty so delete it    
		return dir.delete();
	}


	@Override
	public FileNameGenerator getFileNameGenerator(){
		return config.getFileNameGenerator();
	}

}
