package main.com.ide.packages;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.GridLayout;

import main.com.ide.Properties;

public class LibraryPackagePane extends PackagePane {

	private static final long serialVersionUID = 1L;

	public LibraryPackagePane(Properties properties) {
		super(properties);
		int rows = Packages.libraries.length;

		setSize(590,450);
		setLayout(new GridLayout(rows,1));
		setBackground(new Color(217, 217, 217));

		addPanelPackageList(Packages.libraries);
	
	}
	
	public void updatePackageSelection() {
		
		if (packageGroup == null) {
			return;
		}
		
		for (Checkbox packageBox : packageGroup){
			String thePackage = packageBox.getLabel();
			
			if (properties.getPackagesToImport().contains(thePackage)) {
				packageBox.setState(true);
			}
		}
	}

}
