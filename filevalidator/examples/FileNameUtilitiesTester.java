import com.amdocs.filevalidator.core.FileValidator;
import com.amdocs.filevalidator.core.FileValidatorImpl;
import com.amdocs.filevalidator.exceptions.FilenameGenerationException;
import com.amdocs.filevalidator.securityutilities.FileNameGenerator;


public class FileNameUtilitiesTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		FileValidator fuv = FileValidatorImpl.getInstance();
		FileNameGenerator fileNameGen = fuv.getFileNameGenerator();
		
		System.out.println("Censor filename demonstration :");
		System.out.println("===============================");
		
		String[] filenames = { "regular.txt", "regularregularregularregularregularregularregular.txt", 
				"file1234example5678.txt", "12345.txt", "file_with-other.chars.txt", "file_with^other.chars.txt"};
		for (String filename : filenames) {
			try { 
				System.out.println(filename + " => " + fileNameGen.censorFilename(filename));
			} catch (FilenameGenerationException e) { 
				System.out.println(filename + " generated exception : " + e.getMessage());
			}
		}
		
		System.out.println("\n\n");
		
		System.out.println("Generate filenames demonstration :");
		System.out.println("==================================");
		
		for (int i=0 ; i<10; i++) { 
			System.out.println("GENERATED FILENAME : " + fileNameGen.generateNewRandomFilename());
		}
		
	}

}
