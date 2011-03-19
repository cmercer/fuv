import java.io.FileInputStream;
import java.util.Scanner;

import com.amdocs.filevalidator.securityutilities.SizeBoundedInputStream;


public class SizeBoundedISTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		if (args.length!=2) { 
			System.err.println("Usage : SizeBoundedISTester <bound> <input filename>");
			System.exit(1);
		}
		
		Integer sizeBound = Integer.parseInt(args[0]);
		String inputFilename = args[1];
		
		SizeBoundedInputStream sbis = new SizeBoundedInputStream(new FileInputStream(inputFilename), sizeBound);
		Scanner s = new Scanner(sbis);
		
		int ctr = 1;
		while (s.hasNextLine()) { 
			System.out.println("LINE #" + ctr++ + " : " + s.nextLine());
		}
		if (sbis.hasReachedLimit()) { 
			System.out.println("SizeBoundedInputStream has reached limit");
		} else { 
			System.out.println("SizeBoundedInputStream hasn't reached limit");
		}
		
	}

}
