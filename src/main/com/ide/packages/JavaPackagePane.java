package main.com.ide.packages;

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
	
}