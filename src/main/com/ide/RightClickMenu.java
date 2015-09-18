package main.com.ide;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

/**
 * Simple class to implement right-click functionality
 * in the text editor.
 * 
 * @author Mike Nowicki
 *
 */
public class RightClickMenu {
	
	private JPopupMenu popupMenu;
	
	public RightClickMenu() {
		
		popupMenu = new JPopupMenu();
		
		JMenuItem cut = new JMenuItem(new DefaultEditorKit.CutAction());
		JMenuItem copy = new JMenuItem(new DefaultEditorKit.CopyAction());
		JMenuItem paste = new JMenuItem(new DefaultEditorKit.PasteAction());
		
		cut.setText("Cut");
		copy.setText("Copy");
		paste.setText("Paste");

		cut.setAccelerator(KeyStroke.getKeyStroke('X', 
				KeyEvent.CTRL_DOWN_MASK));
		copy.setAccelerator(KeyStroke.getKeyStroke('C', 
				KeyEvent.CTRL_DOWN_MASK));
		paste.setAccelerator(KeyStroke.getKeyStroke('V', 
				KeyEvent.CTRL_DOWN_MASK));
		
		popupMenu.add(cut);
		popupMenu.add(copy);
		popupMenu.add(paste);	
	}
	
	public void showRightClickMenu(MouseEvent event) {
		popupMenu.show(event.getComponent(), event.getX(), event.getY());
	}
	
}
