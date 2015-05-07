package buildtools;

import ide.BuildConfiguration;
import ide.Properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PropertiesButtonListener implements ActionListener {

	Properties properties;
	
	public PropertiesButtonListener(Properties properties) {
		this.properties = properties;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		BuildConfiguration bc = new BuildConfiguration(properties);
		bc.createFrame();
	}
}
