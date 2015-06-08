package ide.texteditor;

import ide.mouse.RightClickMenu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TextEditorMouseListener implements MouseListener {

	RightClickMenu rightClickMenu = new RightClickMenu();
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			rightClickMenu.showRightClickMenu(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			rightClickMenu.showRightClickMenu(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

}
