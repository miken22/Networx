package ide.texteditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JTextArea;

public class MessageHandler {

	public static void handleErrorMessage(String errorMessage, Process pro, JTextArea buildlog) throws InterruptedException {
		buildlog.append(errorMessage + "\n");
		pro.waitFor();
	}

	public static void handleProgramMessaage(InputStream ins, JTextArea buildlog) throws IOException {
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
		while ((line = in.readLine()) != null) {
			buildlog.append(line + "\n");
		}
	}
	
}
