package main.com.ide.texteditor;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

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
		this.addKeyListener(this);
		
		UndoManager manager = new UndoManager();
	    getDocument().addUndoableEditListener(manager);

	    UndoAction undoAction = new UndoAction(manager);
	    RedoAction redoAction = new RedoAction(manager);
	    
	    registerKeyboardAction(undoAction, KeyStroke.getKeyStroke(
	            KeyEvent.VK_Z, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
	    registerKeyboardAction(redoAction, KeyStroke.getKeyStroke(
	            KeyEvent.VK_Y, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
	}

	@Override
	/**
	 * An attempt to implement a feature where CTRL + '/' places
	 * comment symbols at the beginning of the line in the editor.
	 * 
	 * CURRENTLY DOES NOT WORK
	 * 
	 */
	public void keyPressed(KeyEvent event) {

		/*
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
					scanPosition += line.length();

					if (line.length() == 0) {
						scanPosition++;
					}

					if (scanPosition >= position) {
						break;
					}
					line = bufReader.readLine();
				}

				if (line.startsWith("//")) {
					line = line.substring(1, line.length());
					setCaretPosition(scanPosition - line.length());
					setText(text.substring(0, scanPosition - 2 - line.length()) +
							line +
							text.substring(scanPosition, text.length()));
				} else {
					line = "//" + line;
					setText(text.substring(0, scanPosition + 2 - line.length()) + 
							line + 
							text.substring(scanPosition, text.length()));
				}

				setCaretPosition(oldCursorPosition);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		 */
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


	// The Undo action
	public class UndoAction extends AbstractAction {

		private UndoManager manager;
		
		public UndoAction(UndoManager manager) {
			this.manager = manager;
		}

		public void actionPerformed(ActionEvent evt) {
			try {
				manager.undo();
			} catch (CannotUndoException e) {
				Toolkit.getDefaultToolkit().beep();
			}
		}

	}

	// The Redo action
	public class RedoAction extends AbstractAction {
		public RedoAction(UndoManager manager) {
			this.manager = manager;
		}

		public void actionPerformed(ActionEvent evt) {
			try {
				manager.redo();
			} catch (CannotRedoException e) {
				Toolkit.getDefaultToolkit().beep();
			}
		}

		private UndoManager manager;

	}

}
