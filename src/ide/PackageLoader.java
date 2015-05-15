package ide;

import java.awt.Checkbox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class PackageLoader {

	JFrame configurationFrame;
	JButton ok;
	JButton cancel;
	
	JLabel header;
	JPanel packagePanel;
	
	List<Checkbox> packageGroup;
	
	Properties properties;
	
	public PackageLoader(Properties properties) {
		this.properties = properties;
	}

	/**
	 * Initialize the frame and all its components.
	 */
	public abstract void createFrame();

	/**
	 * Create the checkboxes, add them to the panel and a list to iterate over late
	 */
	public void addPanelPackageList(String[] packageList) {
		
		for (String jungPackage : packageList) {
			Checkbox packageBox = new Checkbox(jungPackage, false);
			packageGroup.add(packageBox);
			packagePanel.add(packageBox);
		}		
	}

	/**
	 * For each checked box get the label and add it to the list of
	 * properties to import
	 * 
	 * @author Michael Nowicki
	 *
	 */
	public class SavePackageListener implements ActionListener {

		private List<Checkbox> panelCBGroup;
		private Properties projectProperties;
		
		public SavePackageListener(List<Checkbox> packageGroup, Properties projectProperties) {
			panelCBGroup = packageGroup;
			this.projectProperties = projectProperties;
		}

		@Override
		public void actionPerformed(ActionEvent save) {
			for (Checkbox option : panelCBGroup) {
				if (option.getState()) {
					projectProperties.addPackage(option.getLabel());
				}
			}
			configurationFrame.dispose();
		}
	}
	
	/**
	 * Close the frame, save nothing
	 * 
	 * @author Michael Nowicki
	 *
	 */
	public class CancelListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent cancel) {
			configurationFrame.dispose();
		}
	}
	
}
