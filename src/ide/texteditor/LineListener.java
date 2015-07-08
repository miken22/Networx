package ide.texteditor;

import ide.mouse.RightClickMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Do not use
 * 
 * @author Mike Nowicki
 *
 */
public class LineListener implements DocumentListener, ActionListener, MouseListener, KeyListener {

	private JTextPane worksheet;
	private JTextArea lineCounter;
	private JLabel lineNumber;

	private RightClickMenu rightClickMenu = new RightClickMenu();
	
	public LineListener(JTextPane worksheet, JTextArea lines, JLabel lineNumber) {
		this.worksheet = worksheet;
		this.lineCounter = lines;
		this.lineNumber = lineNumber;
	}
	
	public String updateLineNumbers() {
		String text = "";
		String document = worksheet.getText();
		int cursorPosition = worksheet.getCaretPosition();
		if (cursorPosition <= document.length()) {
			String upToCurrentSpot = document.substring(0, worksheet.getCaretPosition());
			String[] cursonLineNumber = upToCurrentSpot.split("\n");
			lineNumber.setText(String.valueOf(cursonLineNumber.length));	
		}
		
		String[] documentLines = document.split("\n");
		for (int i = 1; i <= documentLines.length+2; i++) {
			text += i + "\n";
		}		
		return text;
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		lineCounter.setText(updateLineNumbers());
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		lineCounter.setText(updateLineNumbers());
	}
	@Override
	public void changedUpdate(DocumentEvent e) {
		lineCounter.setText(updateLineNumbers());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		lineCounter.setText(updateLineNumbers());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		lineCounter.setText(updateLineNumbers());
		if (e.getButton() == MouseEvent.BUTTON3) {
			rightClickMenu.showRightClickMenu(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		lineCounter.setText(updateLineNumbers());
		if (e.getButton() == MouseEvent.BUTTON3) {
			rightClickMenu.showRightClickMenu(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		lineCounter.setText(updateLineNumbers());
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) {	}

	@Override
	public void keyTyped(KeyEvent e) {
		lineCounter.setText(updateLineNumbers());
		
	}

	@Override
	public void keyPressed(KeyEvent e) {	
		lineCounter.setText(updateLineNumbers());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		lineCounter.setText(updateLineNumbers());
	}	
}
