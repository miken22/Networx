package main.com.ide.packages;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

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

	private PackagePane libPackagePane;
	private PackagePane javaPackagePane;
	private PackagePane jungPackagePane;

	private Properties properties;

	public PackageLoader(Properties properties) {

		super("Packages to Import");
		this.properties = properties;

		// Colour the selected tabs main colour 
		UIManager.put("TabbedPane.selected", new Color(222, 222, 222));
		// Colour top of the tab
		UIManager.put("TabbedPane.highlight", new Color(252, 252, 252));
		// Colour the border around the tabbed pane
		UIManager.put("TabbedPane.contentAreaColor", new Color(252, 252, 252)); 	
	
		// Create modified JTabbedPane so the label text colour can change
		// when the tab is selected/deselected
		optionPane = new JTabbedPane(){
			private static final long serialVersionUID = 1L;
			public Color getForegroundAt(int index){
				if (getSelectedIndex() == index) {
					return Color.BLACK;
				}
				return Color.WHITE;
			}
		};

		ok = new JButton("OK");
		cancel = new JButton("Cancel");

		topPanel = new JPanel();

		libPackagePane = new PackagePane(properties, "LIB");
		javaPackagePane = new PackagePane(properties, "JAVA");
		jungPackagePane = new PackagePane(properties, "JUNG");

		initialize();

	}

	private void initialize() {

		setSize(550,700);
		setLayout(null);
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		add(topPanel);

		// Add panel and tabbed pane, load each pane
		topPanel.setBounds(5,5,540, 600);
		topPanel.setLayout(new BorderLayout());

		// Go through each set of packages and pre-check
		// each item already set to import so the user can
		// see their settings		
		libPackagePane.updatePackageSelection();
		javaPackagePane.updatePackageSelection();
		jungPackagePane.updatePackageSelection();	

		// Create each scroll pane and add it to the frame

		JScrollPane fuzzyPane = new JScrollPane(libPackagePane);

		JScrollPane javaPane = new JScrollPane(javaPackagePane);
		javaPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JScrollPane jungPane = new JScrollPane(jungPackagePane);
		jungPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);          

		optionPane.add("Fuzzy Packages", fuzzyPane);
		optionPane.add("Java Packages", javaPane);
		optionPane.add("JUNG2 Packages", jungPane);

		optionPane.setBackground(new Color(72, 72, 72));
		optionPane.setForeground(new Color(255, 255, 255));

		topPanel.add(optionPane, BorderLayout.CENTER);

		// Load buttons and their listeners

		ok.setBounds(150, 625, 90, 35);
		cancel.setBounds(275, 625, 90, 35);

		ok.addActionListener(new SavePackageListener(properties));
		ok.setBackground(new Color(252, 252, 252));

		cancel.addActionListener(new CancelListener());
		cancel.setBackground(new Color(239, 54, 54));

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
