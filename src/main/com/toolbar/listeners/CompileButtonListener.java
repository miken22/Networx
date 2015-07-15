package main.com.toolbar.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import main.com.builder.ScriptBuilder;
import main.com.builder.ScriptCompiler;
import main.com.ide.Properties;

/**
 * Class that implements the compiling functionality. Saves the script, calls javac
 * to compile the code and then uses java to execute the program. Text output is 
 * displayed in the build log text area.
 * 
 * @author Mike Nowicki
 *
 */
public class CompileButtonListener implements ActionListener {

	// The IDE's notepad and outputs
	private JTextPane worksheet;
	private JTextArea buildlog;

	/**
	 * Class that extracts the text from the worksheet and build
	 * class(es) as needed that can be compiled and executed.
	 */
	
	private Properties properties;

	public CompileButtonListener(JTextPane worksheet, JTextArea buildlog, Properties properties) {
		this.worksheet = worksheet;
		this.buildlog = buildlog;	
		this.properties = properties;
	}

	/**
	 * Attempts to compile the user script
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		
		// Set first file for class path / compiling. Currently not needed
//		String programFiles = ".UserFiles/UserScript.java";
//		String programClasses = "UserScript";
		
		ScriptBuilder sb = new ScriptBuilder(properties);
		ScriptCompiler compiler = new ScriptCompiler(properties, worksheet, buildlog);
		
		// Don't compile anything if the script is blank
		if (worksheet.getText().length() == 0) {
			buildlog.setText("Nothing to compile!");
			return;
		} else {
			// Clear build log
			buildlog.setText("");	
			// Extract code, separate methods/classes, and compile
			try {
				sb.buildScript(worksheet);
				compiler.compileAndRun();
			} catch (Exception e) { /* Should not have issues here */ }
		}
	}
}
