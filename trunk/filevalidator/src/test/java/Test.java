import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amdocs.filevalidator.config.ConfigBean;
import com.amdocs.filevalidator.config.ConfigManager;
import com.amdocs.filevalidator.core.FileValidator;
import com.amdocs.filevalidator.core.FileValidatorImpl;
import com.amdocs.filevalidator.modules.ModuleImpl;
import com.amdocs.filevalidator.modules.UnixFilePermissionsModule;
import com.amdocs.filevalidator.securityutilities.SizeBoundedInputStream;


public class Test {
	
	private static Logger logger = LoggerFactory.getLogger(Test.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		FileValidator fv = FileValidatorImpl.getInstance();

		ConfigBean config = ConfigManager.getInstance().getConfigBean();
		for (ModuleImpl module : config.getModules()) { 
			if (module instanceof UnixFilePermissionsModule) { 
				for (int i=1 ; i<6 ; i++) {
					File f = new File("/tmp/file" + i);
					boolean res = module.validate(f.getAbsolutePath(), f.getName());
					System.out.println(f.getName() + " = " + res);
				}
				
			}
		}
		
//		File f3 = new File("C:\\tmp_rotem\\tmp\\test_file.tar");
//		File f3 = new File("C:\\tmp_rotem\\tmp\\dir_tar.tar");
		File f3 = new File("C:\\tmp_rotem\\tmp\\out.zip");
//		
//		System.out.println(fv.validate(f3));
		System.exit(0);
		
		FileInputStream fis = new FileInputStream(f3);
				
		
		BufferedInputStream bis = new BufferedInputStream(fis);		 		
		try {			
			ArchiveInputStream input = 
				new ArchiveStreamFactory().createArchiveInputStream(bis);
			
			if (input instanceof ZipArchiveInputStream) {
				// ZIP
				logger.info("Found ZIP file");
				
				ZipInputStream zis = new ZipInputStream(bis);
				ZipEntry entry = zis.getNextEntry();
				while (entry != null) {
					String name = entry.getName();
					logger.info("Entry: " + name + (entry.isDirectory() ? "(directory). Skipping..." : ""));
					System.out.println("Entry: " + name + (entry.isDirectory() ? "(directory). Skipping..." : ""));
					if (!entry.isDirectory()) {
						int c;
						while ((c=zis.read()) != -1) {
							System.out.print((char)c);
						}						
					}					
					entry = zis.getNextEntry();
				}
				

				
				
			} else if (input instanceof TarArchiveInputStream) {
				// TAR
				logger.info("Found TAR file");
				
				TarArchiveInputStream tarInput = (TarArchiveInputStream)input; 
				TarArchiveEntry entry = tarInput.getNextTarEntry();
				int offset = 0;
				while (entry != null) {
					String name = entry.getName();
					logger.info("Entry: " + name + (entry.isDirectory() ? "(directory). Skipping..." : ""));
					System.out.println("Entry: " + name + (entry.isDirectory() ? "(directory). Skipping..." : ""));
					if (!entry.isDirectory()) { 
						int size = 0;
						byte[] content = new byte[1];
						int c;
						while (size < entry.getSize()) {
							c = tarInput.read();
//						    c = tarInput.read(content, offset, 1);
						    System.out.print((char)c);
						    size++;						    
						}						
					}	
					offset+= entry.getSize();
					entry = tarInput.getNextTarEntry();
				}

				
				
			} else {
				logger.warn("AR/JAR/CPIO Archives files are not supported. Ignoring...");
			}			
		} catch (ArchiveException e) {
			// Not ZIP and not TAR
			logger.debug("Not ZIP/TAR");
			
			// check for BZIP
			bis.mark(4);
			try {
				BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(bis);
				logger.info("Found BZIP file");
				int c = 0;
				while ((c = bzIn.read()) != -1) {
					System.out.print((char)c);    
				}						
			} catch (IOException ex) {
				logger.debug("Not BZIP");
				bis.reset();
			}
			
			
			// check for GZIP
			bis.mark(4);
			try {
				GZIPInputStream gzIn = new GZIPInputStream(bis);			
//				GzipCompressorInputStream gzIn = new GzipCompressorInputStream(bis);
				logger.info("Found GZIP file");
				int c = 0;
				while ((c = gzIn.read()) != -1) {
					System.out.print((char)c);    
				}						
			} catch (IOException ex) {
				logger.debug("Not GZIP");
				bis.reset();
			}
		}



		
		System.exit(0);
		

		File f1 = new File("C:\\file.txt");
		Scanner s2 = new Scanner(f1);
		while (s2.hasNext()) { System.out.println("ORIG  - " + s2.nextLine()); }
		
		SizeBoundedInputStream is = new SizeBoundedInputStream(new FileInputStream(f1));
		
		Scanner s = new Scanner(is);
		
		while (s.hasNext()) {
			System.out.println("LINE - " + s.nextLine() + " " + is.hasReachedLimit());
		}
		
		System.exit(0);
		
		// update test
		
		//FileValidator fv = FileValidatorImpl.getInstance();
		
		File f = File.createTempFile("abc", "abc");
		System.out.println("fv says : " + fv.validate(f));
		
		File f2 = File.createTempFile("def", "abc.doc");
		System.out.println("fv says : " + fv.validate(f2));
		
		//for (Module m : ConfigManager.getInstance().getConfigBean().getModules()) { 
		//	System.out.println(m.getName() + " - scan inner files: " + m.scanInnerFiles());
		//}
		
		
		
	}

}
