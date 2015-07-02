package ide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class MessageHandler {

	public static void handleErrorMessage(String errorMessage, JTextPane worksheet, JTextArea buildlog) throws InterruptedException {
		if (errorMessage.matches(".*[0-9].*")) {
			try {
				errorMessage = adjustErrorIndex(errorMessage, worksheet);
				buildlog.append(errorMessage + "\n");
				return;
			} catch (IOException e) {
				buildlog.append("Something went wrong compiling script, ensure ");
				buildlog.append("the command line arguments are correct. ");
			}
		}

		buildlog.append(errorMessage + "\n");
	}

	public static void handleProgramMessaage(InputStream ins, JTextArea buildlog) throws IOException {
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
		while ((line = in.readLine()) != null) {
			buildlog.append(line + "\n");
		}
	}

	private static String adjustErrorIndex(String errorMessage, JTextPane worksheet) throws IOException {

		String[] parts = errorMessage.split(":");
		String script = worksheet.getText();
		String[] scriptLines = script.split("\n");

		File directory = new File("UserFiles");
		directory.deleteOnExit();

		// UserFile is 10 characters long, so start from there to the period
		// in the name ******.java
		String errorFile = parts[0].substring(10,parts[0].indexOf('.')) + ".java";

		File userScript = new File(directory, errorFile);
		userScript.deleteOnExit();

		FileReader reader = new FileReader(userScript);
		BufferedReader bufferedReader = new BufferedReader(reader);

		Integer initialLineNumber = Integer.valueOf(parts[1]);

		String line = bufferedReader.readLine();
		int lineCounter = 1;
		while (lineCounter < initialLineNumber) {
			lineCounter++;
			line = bufferedReader.readLine();
		}

		for (int i = 0; i < scriptLines.length; i++) {
			if (scriptLines[i].trim().equals(line.trim())) {
				bufferedReader.close();
				return parts[0] + ":" + (i+1) + parts[2] + parts[3];
			}
		}

		bufferedReader.close();
		return errorMessage;
	}

}
