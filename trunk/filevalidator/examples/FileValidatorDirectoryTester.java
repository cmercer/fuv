import java.io.File;
import java.io.IOException;

import com.amdocs.filevalidator.core.FileValidator;
import com.amdocs.filevalidator.core.FileValidatorImpl;


public class FileValidatorDirectoryTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// check args
		if (args.length != 1) {
			System.err.println("Usage : " + FileValidatorDirectoryTester.class.getSimpleName() + " [ directory name ]");
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
		
		FileValidator fuv = FileValidatorImpl.getInstance();
				
		System.out.println("Testing directory " + dir.getAbsolutePath());
		System.out.println("===================================================");
		
		for (File file : dir.listFiles()) { 
			boolean isValid;
			try {
				isValid = fuv.validate(file);
				System.out.println(file.getName()+ " is " + (isValid?"":"NOT ") + "valid");
			} catch (IOException e) {
				System.err.println("Error while validating " + file.getName() + ". " + e.getMessage());
			}
		}
	}

}
