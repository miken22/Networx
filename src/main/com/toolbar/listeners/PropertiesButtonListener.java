package main.com.toolbar.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.com.ide.Properties;
import main.com.ide.packages.JavaPackageLoader;
import main.com.ide.packages.JungPackageLoader;
import main.com.ide.packages.LibraryPackageLoader;
import main.com.ide.packages.PackageLoader;

/**
 * Class that creates the correct java package loader frame
 * 
 * @author Mike Nowicki
 *
 */
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
			// Add java package list
			PackageLoader jpl = new JavaPackageLoader(properties);
			jpl.createFrame();
		} else if (id == 1) {
			// Load jung package list
			PackageLoader bc = new JungPackageLoader(properties);
			bc.createFrame();
		} else if (id == 2) {
			// Load library package list
			PackageLoader lpl = new LibraryPackageLoader(properties);
			lpl.createFrame();
		}
		
	}
}
