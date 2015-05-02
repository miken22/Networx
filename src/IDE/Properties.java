package IDE;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple class to store the list of packages a user wants to include in the script
 * 
 * @author Michael Nowicki
 *
 */
public class Properties {

	List<String> packagesToImport;
	
	public Properties() {
		packagesToImport = new ArrayList<>();
	}
	
	public void addPackage(String jungPackage) {
		packagesToImport.add(jungPackage);
	}
	
	public List<String> getPackagesToImport() {
		return packagesToImport;
	}
	
}
