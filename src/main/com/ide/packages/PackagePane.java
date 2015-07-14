package main.com.ide.packages;

import java.awt.Checkbox;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import main.com.ide.Properties;

public abstract class PackagePane extends JPanel {

	Properties properties;
	
	List<Checkbox> packageGroup;
	
	private static final long serialVersionUID = 1L;

	public PackagePane(Properties properties) {
		this.properties = properties;
	}

	/**
	 * Create the checkboxes, add them to the panel and a list to iterate over late
	 */
	public void addPanelPackageList(String[] packageList) {
		
		if (packageGroup == null) {
			packageGroup = new ArrayList<>();
		}
		
		for (String thePackage : packageList) {
			Checkbox packageBox = new Checkbox(thePackage, false);
			packageGroup.add(packageBox);
			
			if (properties.getPackagesToImport().contains(thePackage)) {
				packageBox.setState(true);
			}
			
			add(packageBox);
		}		
	}

	/**
	 * 
	 * @return A collection of the checkboxes that belong to that pane
	 */
	public List<Checkbox> getPackageGroup() {
		return packageGroup;
	}
	
	/**
	 * Call this method to update the selected checkboxes to
	 * present the user with their current selections
	 */
	public abstract void updatePackageSelection();
}
