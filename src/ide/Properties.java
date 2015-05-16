package ide;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple class to store the list of packages a user wants to include in the script
 * 
 * @author Michael Nowicki
 *
 */
public class Properties {

	private List<String> packagesToImport;
	
	public Properties() {
		packagesToImport = new ArrayList<>();
	}
	
	public void addPackage(String requiredPackage) {
		packagesToImport.add(requiredPackage);
	}
	
	public List<String> getPackagesToImport() {
		return packagesToImport;
	}
	
}
