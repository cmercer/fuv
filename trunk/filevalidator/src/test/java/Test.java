import java.io.File;

import com.amdocs.filevalidator.core.FileValidator;
import com.amdocs.filevalidator.core.FileValidatorImpl;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		FileValidator fv = FileValidatorImpl.getInstance();
		
		File f = File.createTempFile("abc", "abc");
		System.out.println("fv says : " + fv.validate(f));
		
		
		
	}

}
