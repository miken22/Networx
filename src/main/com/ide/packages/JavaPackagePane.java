package main.com.ide.packages;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.GridLayout;

import main.com.ide.Properties;

public class JavaPackagePane  extends PackagePane {

	private static final long serialVersionUID = 1L;
	
	public JavaPackagePane(Properties properties) {
		super(properties);
		
		int rows = Packages.javaPackages.length;
		
		setLayout(new GridLayout(rows,1));
		setBackground(new Color(217, 217, 217));
		
		addPanelPackageList(Packages.javaPackages);
        
	}
	
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