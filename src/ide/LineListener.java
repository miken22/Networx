package ide;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

public class LineListener implements DocumentListener{

	JTextPane worksheet;
	JTextArea lines;
	
	public LineListener(JTextPane worksheet, JTextArea lines) {
		this.worksheet = worksheet;
		this.lines = lines;
	}
	
	public String getText() {
		int caretPosition = worksheet.getDocument().getLength();
		Element root = worksheet.getDocument().getDefaultRootElement();
		String text = "1" + System.getProperty("line.separator");
		for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++){
			text += i + System.getProperty("line.separator");
		}
		return text;
	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		lines.setText(getText());
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		lines.setText(getText());
	}
	@Override
	public void changedUpdate(DocumentEvent e) {
		lines.setText(getText());
	}	
}
