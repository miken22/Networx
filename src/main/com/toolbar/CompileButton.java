package main.com.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import main.com.builder.ScriptBuilder;
import main.com.builder.ScriptCompiler;
import main.com.ide.Properties;

/**
 * Default button for the compile functionality, is basic
 * now, meant for future development
 * 
 * @author Mike Nowicki
 *
 */
public class CompileButton extends JButton implements ActionListener {

	private static final long serialVersionUID = 1L;

	// The IDE's notepad and outputs
	private JTextPane worksheet;
	private JTextArea buildlog;

	/**
	 * Class that extracts the text from the worksheet and build
	 * class(es) as needed that can be compiled and executed.
	 */

	private Properties properties;

	public CompileButton(JTextPane worksheet, JTextArea buildlog, 
						 Properties properties) {
		super();

		this.worksheet = worksheet;
		this.buildlog = buildlog;	
		this.properties = properties;
		
		addActionListener(this);

	}


	public CompileButton(JTextPane worksheet, JTextArea buildlog, 
						 Properties properties, String name) {
		super(name);

		this.worksheet = worksheet;
		this.buildlog = buildlog;	
		this.properties = properties;

		addActionListener(this);
		
	}


	/**
	 * Attempts to compile the user script
	 */
	@Override
	public void actionPerformed(ActionEvent event) {

		ScriptBuilder sb = new ScriptBuilder(properties);
		ScriptCompiler compiler = new ScriptCompiler(properties, 
				worksheet,
				buildlog);

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
			} catch (Exception e) {
				// If an error occurs compiling or running it is
				// due to java or javac not being runnable on the command
				// line, tell the user to verify that.
				JOptionPane.showMessageDialog(null, 
						"Error compiling or running script, verify javac " +
								"and java can be invoked from the command line.",
								"Execution Error",
								JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
