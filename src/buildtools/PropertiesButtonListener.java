package buildtools;

import ide.JavaPackageLoader;
import ide.JungPackageLoader;
import ide.Properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PropertiesButtonListener implements ActionListener {

	Properties properties;
	int id;
	
	public PropertiesButtonListener(Properties properties, int id) {
		this.properties = properties;
		this.id = id;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (id == 0) {
			// Add java imports
			JavaPackageLoader jpl = new JavaPackageLoader(properties);
			jpl.createFrame();
		} else if (id == 1) {
			// Load jung packages
			JungPackageLoader bc = new JungPackageLoader(properties);
			bc.createFrame();
		}
		
	}
}
