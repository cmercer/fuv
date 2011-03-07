import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ContentHandlerDecorator;


public class TikaTypeDirectoryTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// check args
		if (args.length != 1) {
			System.err.println("Usage : " + TikaTypeDirectoryTester.class.getSimpleName() + " [ directory name ]");
			System.exit(-1);
		}

		File dir = new File(args[0]);
		if (!dir.exists()) { 
			System.err.println("Directory " + dir.getAbsolutePath() + " doesn't exist");
			System.exit(-1);
		}
		
		if (!dir.isDirectory()) { 
			System.err.println("Directory " + dir.getAbsolutePath() + " is not a directory");
			System.exit(-1);
		}
		
		System.out.println("Testing directory " + dir.getAbsolutePath());
		System.out.println("===================================================");
		
		for (File file : dir.listFiles()) { 
			String fileName = file.getName();
			
			ContentHandlerDecorator contenthandler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, fileName);
			Parser parser = new AutoDetectParser();
			FileInputStream in = null;
			try {
				in = new FileInputStream(file);
				parser.parse(in, contenthandler, metadata, new ParseContext());
			} catch (Exception e) {
				System.err.println("Error while running tika on " + fileName + ". " + e.getMessage());
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						System.err.println("Can't close file");
						System.exit(-1);
					}
				}
			}
			String contentType = metadata.get(Metadata.CONTENT_TYPE);

			System.out.println(fileName + "\t" + contentType);
			
		}
	}

}
