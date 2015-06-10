package ide;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class LoadingPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private	BufferedImage image;
	
	public LoadingPanel() {
		super();
		 
		try {
	        image = ImageIO.read(this.getClass().getResource("/resources/splash.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadScreen();
	}

	private void loadScreen() {

		JLabel header = new JLabel();
		header.setText("Loading Networx");
		header.setBounds(0,0,360, 50);
		
		JLabel jLabel = new JLabel(new ImageIcon(image));
		jLabel.setBounds(50, 0, 360, 317);
		setSize(360, 317);
		
		add(header);
		add(jLabel);
		repaint();
		
	}
}
