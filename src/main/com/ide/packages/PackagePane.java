package main.com.ide.packages;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import main.com.ide.Properties;

public class PackagePane extends JPanel {

	// Used to determine which should be pre-checked
	Properties properties;
	// List of checkboxes that have been selected
	List<Checkbox> packageGroup;
	
	private static final long serialVersionUID = 1L;

	public PackagePane(Properties properties, String type) {
		this.properties = properties;

		int rows = 0;
		
		if (type.equals("LIB")) {
			rows = Packages.libraries.length;

			setSize(590,450);
			setLayout(new GridLayout(rows,1));
			setBackground(new Color(217, 217, 217));

			addPanelPackageList(Packages.libraries);
		} else if (type.equals("JUNG")){
			rows = Packages.jungPackages.length;

			setSize(590,450);
			setLayout(new GridLayout(rows,1));
			setBackground(new Color(217, 217, 217));

			addPanelPackageList(Packages.jungPackages);
		} else if (type.equals("JAVA")) {
			rows = Packages.javaPackages.length;

			setSize(590,450);
			setLayout(new GridLayout(rows,1));
			setBackground(new Color(217, 217, 217));

			addPanelPackageList(Packages.javaPackages);
		}
		
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
	public void updatePackageSelection() {

		if (packageGroup == null) {
			return;
		}
		
		for (Checkbox packageBox : packageGroup){
			String thePackage = packageBox.getName();
			
			if (properties.getPackagesToImport().contains(thePackage)) {
				packageBox.setState(true);
			}
		}
	}
	
}
