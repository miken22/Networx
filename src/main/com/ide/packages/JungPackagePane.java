package main.com.ide.packages;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.GridLayout;

import main.com.ide.Properties;


/**
 * Class that controls a frame to get the user to select the packages to import
 * for their script. This could be extended to make each package expandable, make
 * the classes the check-boxes, rather than importing the whole package.
 * 
 * @author Michael Nowicki
 *
 */
public class JungPackagePane  extends PackagePane {

	private static final long serialVersionUID = 1L;
	
	public JungPackagePane(Properties properties) {
		super(properties);
	
		int rows = Packages.jungPackages.length;
		
		setLayout(new GridLayout(rows,1));
		setBackground(new Color(217, 217, 217));
		
        addPanelPackageList(Packages.jungPackages);
        
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