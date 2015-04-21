package IDE;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class SaveText {

	private static PrintStream outputStream = System.out;
	
	public static void saveWorksheet(String name, String text) throws FileNotFoundException {
		String path = SaveText.class.getProtectionDomain().getCodeSource().getLocation().getPath() + name + ".txt";
		outputStream = new PrintStream(path);
        outputStream.print(text + "\r\n");
	}
}
