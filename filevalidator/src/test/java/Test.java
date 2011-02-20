import java.io.File;

import com.amdocs.filevalidator.core.FileValidator;
import com.amdocs.filevalidator.core.FileValidatorImpl;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		// update test
		
		FileValidator fv = FileValidatorImpl.getInstance();
		
		File f = File.createTempFile("abc", "abc");
		System.out.println("fv says : " + fv.validate(f));
		
		File f2 = File.createTempFile("def", "abc.doc");
		System.out.println("fv says : " + fv.validate(f2));
		
		//for (Module m : ConfigManager.getInstance().getConfigBean().getModules()) { 
		//	System.out.println(m.getName() + " - scan inner files: " + m.scanInnerFiles());
		//}
		
		
		
	}

}
