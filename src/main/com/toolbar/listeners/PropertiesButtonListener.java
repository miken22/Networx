package main.com.toolbar.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.com.ide.Properties;
import main.com.ide.packages.PackageLoader;

/**
 * Class that creates the correct java package loader frame
 * 
 * @author Mike Nowicki
 *
 */
public class PropertiesButtonListener implements ActionListener {

	Properties properties;
	PackageLoader pl;
	
	public PropertiesButtonListener(Properties properties) {
		this.properties = properties;
		pl = new PackageLoader(properties);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		pl.createFrame();
	}
}
