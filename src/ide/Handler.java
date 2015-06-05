package ide;

import javax.swing.JTextArea;

public class Handler {

	public static void handleErrorMessage(String errorMessage, Process pro, JTextArea buildlog) throws InterruptedException {
		buildlog.append(errorMessage + "\n");
		pro.waitFor();
	}

}
