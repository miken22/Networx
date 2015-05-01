package IDE;

import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

public class BuildConfiguration {
	
	JFrame configurationFrame;
	JButton ok;
	JButton cancel;
	
	JPanel packagePanel;
	
	Properties properties;
	
	public BuildConfiguration(Properties properties) {
		this.properties = properties;
	}

	// TODO: Create properties window
	public void createFrame() {
		configurationFrame = new JFrame("Build Properties");
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		packagePanel = new JPanel();
		
		configurationFrame.setSize(400, 700);
		configurationFrame.setResizable(false);
		configurationFrame.setLocationRelativeTo(null);
		
		packagePanel.setBounds(5, 5, 380, 600);
		packagePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		JScrollPane scrollPane = new JScrollPane(packagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(5, 5, 380, 600);
        
        addPanelPackageList();
		
		ok.setBounds(50, 620, 100, 25);
		cancel.setBounds(200, 620, 100, 25);
		
		configurationFrame.setLayout(null);
		
		Container c = configurationFrame.getContentPane();
		c.add(ok);
		c.add(cancel);
		c.add(packagePanel);
		configurationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		configurationFrame.setVisible(true);
	}

	private void addPanelPackageList() {
		
		// TODO: Add list of packages, radio/check boxes to select.
		
	}
	
}
