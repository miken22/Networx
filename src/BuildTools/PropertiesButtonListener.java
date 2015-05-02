package BuildTools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import IDE.BuildConfiguration;
import IDE.Properties;

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
