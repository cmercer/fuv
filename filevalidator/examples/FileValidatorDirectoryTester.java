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
		

		FileValidator fuv = FileValidatorImpl.getInstance();
		
		if (!dir.isDirectory()) { 
			File f = dir;
			validate(fuv, f);
			System.exit(0);
		}
				
		System.out.println("Testing directory " + dir.getAbsolutePath());
		System.out.println("===================================================");
		
		for (File file : dir.listFiles()) { 
			validate(fuv, file);
		}
	}

	private static boolean validate(FileValidator fuv, File file) {
		try {
			boolean isValid = fuv.validate(file);
			System.out.println(file.getName()+ " is " + (isValid?"":"NOT ") + "valid");
			return isValid;
		} catch (IOException e) {
			System.err.println("Error while validating " + file.getName() + ". " + e.getMessage());
			return false;
		}
	}

}
