import java.io.File;

import com.amdocs.filevalidator.config.ConfigManager;
import com.amdocs.filevalidator.core.FileValidator;
import com.amdocs.filevalidator.core.FileValidatorImpl;
import com.amdocs.filevalidator.modules.Module;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		// update test
		
		FileValidator fv = FileValidatorImpl.getInstance();
		
		File f = File.createTempFile("abc", "abc");
		//System.out.println("fv says : " + fv.validate(f));
		
		for (Module m : ConfigManager.getInstance().getConfigBean().getModules()) { 
			System.out.println(m.getName() + " - " + m.scanInnerFiles());
		}
		
		
		
	}

}
