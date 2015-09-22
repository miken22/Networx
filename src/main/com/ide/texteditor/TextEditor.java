package main.com.ide.texteditor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;

/**
 * Overriding JTextPane class to provide more
 * control over the behaviour of the editor
 * 
 * @author Michael Nowicki
 *
 */
public class TextEditor extends JTextPane implements KeyListener{

	private static final long serialVersionUID = 1L;

	public TextEditor(DefaultStyledDocument document) {
		super(document);
		// Functionality currently does not work, to do later
//		this.addKeyListener(this);
	}

	@Override
	/**
	 * An attempt to implement a feature where CTRL + '/' places
	 * comment symbols at the beginning of the line in the editor.
	 * 
	 * CURRENTLY REMOVING COMMENT SYMBOL DOES NOT WORK
	 * 
	 */
	public void keyPressed(KeyEvent event) {

		
		int oldCursorPosition = 0;

		if (event.isControlDown() && event.getKeyCode() == 47){

			int position = getCaretPosition();
			oldCursorPosition = position;

			if (position == 0) {
				setText("//" + getText());
				return;
			}

			String text = getText();

			// Buffer the text to iterate through faster
			BufferedReader bufReader = new BufferedReader(new StringReader(text));
			int scanPosition = 0;
			try {
				String line = bufReader.readLine();

				while (line != null) {
					scanPosition += line.length() + 1;

					if (line.length() == 0) {
						scanPosition++;
					}

					if (scanPosition >= position) {
						break;
					}
					line = bufReader.readLine();
				}

				if (line.startsWith("//")) {
					line = line.substring(2, line.length());					
					int substringPos = scanPosition - line.length() - 1;
					
					setText(text.substring(0, substringPos) +
							line +
							text.substring(substringPos + line.length() + 1,
									       text.length()));

					// Set into new position, account for two new chars on line
//					setCaretPosition(oldCursorPosition + 2);
				} else {
					line = "//" + line;
					setText(text.substring(0, scanPosition + 1 - line.length() - 2) + 
							line + 
							text.substring(scanPosition, text.length()));
					// Set caret to same position, account for removing 2 chars
					setCaretPosition(oldCursorPosition-2);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}

	/**************************************************************
	 * 
	 * Not used
	 * 
	 **************************************************************/

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

}
