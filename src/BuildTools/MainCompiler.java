package BuildTools;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import IDE.SaveText;

public class MainCompiler {
	
	PrintStream outputStream = System.out;
	
	public void buildScript(String script) {
		String path = SaveText.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "USERSCRIPT.txt";
		try {
			outputStream = new PrintStream(path);

			outputStream.print("public class UserScript { \r\n");
			outputStream.print("public static void main(String[] args) { \r\n");
			
	        outputStream.print(script + "\r\n} ");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
