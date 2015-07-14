package main.com.ide.mouse;

import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
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
	private JMenuItem cut;
	private JMenuItem copy;
	private JMenuItem paste;
	

	public RightClickMenu() {
		popupMenu = new JPopupMenu();
		cut = new JMenuItem(new DefaultEditorKit.CutAction());
		copy = new JMenuItem(new DefaultEditorKit.CopyAction());
		paste = new JMenuItem(new DefaultEditorKit.PasteAction());
		
		cut.setText("Cut");
		copy.setText("Copy");
		paste.setText("Paste");
		
		popupMenu.add(cut);
		popupMenu.add(copy);
		popupMenu.add(paste);	
	}
	
	public void showRightClickMenu(MouseEvent event) {
		popupMenu.show(event.getComponent(), event.getX(), event.getY());
	}
	
}
