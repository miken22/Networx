package ide;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

public class JavaPackageLoader {

	JFrame configurationFrame;
	JButton ok;
	JButton cancel;
	
	JLabel header;
	JPanel packagePanel;
	
	List<Checkbox> packageGroup;
	
	Properties properties;
	
	public JavaPackageLoader(Properties properties) {
		this.properties = properties;
	}

	/**
	 * Initialize the frame and all its components.
	 */
	public void createFrame() {
		configurationFrame = new JFrame("Build Properties");
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		header = new JLabel("Select JAR's needed for script.");
		packagePanel = new JPanel();
		packageGroup = new ArrayList<>();
				
		int rows = JavaPackages.javaPackages.length;
		
		configurationFrame.setSize(400, 700);
		configurationFrame.setResizable(false);
		configurationFrame.setLocationRelativeTo(null);
		configurationFrame.setLayout(null);

		header.setBounds(5, 5, 380, 45);
		header.setBackground(new Color(217, 217, 217));
		
		packagePanel.setBounds(5, 50, 380, 550);
		packagePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		packagePanel.setLayout(new GridLayout(rows,1));
		
        addPanelPackageList();
        
		JScrollPane scrollPane = new JScrollPane(packagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(5, 50, 380, 550);
		
		ok.setBounds(50, 620, 100, 25);
		ok.addActionListener(new SavePackageListener(packageGroup, properties));
		cancel.setBounds(200, 620, 100, 25);
		cancel.addActionListener(new CancelListener());
		
		Container c = configurationFrame.getContentPane();
		c.add(ok);
		c.add(cancel);
		c.add(header);
		c.add(packagePanel);
		
		c.setBackground(new Color(217, 217, 217));
		
		configurationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		configurationFrame.setVisible(true);
	}

	/**
	 * Create the checkboxes, add them to the panel and a list to iterate over late
	 */
	private void addPanelPackageList() {

		for (String jungPackage : JavaPackages.javaPackages) {
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
	private class SavePackageListener implements ActionListener {

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
