package main.com.ide.packages;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import main.com.ide.Properties;

/**
 * Main package loader class to display packages
 * as checkboxes for import.
 * 
 * @author Mike Nowicki
 *
 */
public class PackageLoader extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTabbedPane optionPane;
	private JButton ok;
	private JButton cancel;
	
	private JPanel topPanel;
	
	private LibraryPackagePane libPackagePane;
	private JavaPackagePane javaPackagePane;
	private JungPackagePane jungPackagePane;
	
	private Properties properties;
	
	public PackageLoader(Properties properties) {
		
		super("Packages to Import");
		
		this.properties = properties;
	
		optionPane = new JTabbedPane();
		
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		
		topPanel = new JPanel();
		
		libPackagePane = new LibraryPackagePane(properties);
		javaPackagePane = new JavaPackagePane(properties);
		jungPackagePane = new JungPackagePane(properties);
		
		initialize();
		
	}

	private void initialize() {
		
		setSize(550,700);
		setLayout(null);
		setResizable(false);
		
		add(topPanel);

		// Add panel and tabbed pane, load each pane
		topPanel.setBounds(5,5,540, 600);
		topPanel.setLayout(new BorderLayout());

		libPackagePane.updatePackageSelection();
		javaPackagePane.updatePackageSelection();
		jungPackagePane.updatePackageSelection();	
		
		JScrollPane fuzzyPane = new JScrollPane(libPackagePane);

        JScrollPane javaPane = new JScrollPane(javaPackagePane);
        javaPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JScrollPane jungPane = new JScrollPane(jungPackagePane);
        jungPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);          
		
		optionPane.add("Fuzzy Packages", fuzzyPane);
		optionPane.add("Java Packages", javaPane);
		optionPane.add("JUNG2 Packages", jungPane);
		
		topPanel.add(optionPane, BorderLayout.CENTER);

		// Load buttons and their listeners

		ok.setBounds(150, 625, 90, 35);
		cancel.setBounds(275, 625, 90, 35);
		
		ok.addActionListener(new SavePackageListener(properties));
		cancel.addActionListener(new CancelListener());
		
		add(ok);
		add(cancel);	
		
	}

	/**
	 * Show the frame.
	 */
	public void createFrame() {
		
		libPackagePane.updatePackageSelection();
		javaPackagePane.updatePackageSelection();
		jungPackagePane.updatePackageSelection();
		
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	/**
	 * For each checked box get the label and add it to the list of
	 * properties to import
	 * 
	 * @author Michael Nowicki
	 *
	 */
	public class SavePackageListener implements ActionListener {

		private Properties projectProperties;
		
		public SavePackageListener(Properties projectProperties) {
			this.projectProperties = projectProperties;
		}

		@Override
		public void actionPerformed(ActionEvent save) {

			projectProperties.clearImports();
			
			List<Checkbox> panelGroup = libPackagePane.getPackageGroup();
			
			// Look through all components sets of checkboxes and store
			// any that are selected
			
			for (Checkbox option : panelGroup) {
				if (option.getState()) {
					projectProperties.addPackage(option.getLabel());
				}
			}
			
			panelGroup = javaPackagePane.getPackageGroup();
			for (Checkbox option : panelGroup) {
				if (option.getState()) {
					projectProperties.addPackage(option.getLabel());
				}
			}
			
			
			panelGroup = jungPackagePane.getPackageGroup();
			for (Checkbox option : panelGroup) {
				if (option.getState()) {
					projectProperties.addPackage(option.getLabel());
				}
			}
			dispose();
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
			dispose();
		}
	}
	
}
