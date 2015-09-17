package main.com.ide.packages;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import main.com.ide.Properties;

/**
 * Class that extends the general menu item class for creating the properties
 * frame. Implements its own action listener to initiate the frame creation.
 * 
 * @author Michael Nowicki
 *
 */
public class PackageMenuItem extends JMenuItem implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Properties properties;

	public PackageMenuItem(Properties properties, String label) {
		super(label);
		this.properties = properties;
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				PackageLoader pl = new PackageLoader(properties);
				pl.createFrame();		
			}
		}); 		
	}		
}
