package main.com.ide;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import main.com.ide.mouse.RightClickListener;
import main.com.ide.texteditor.TextEditorDocument;
import main.com.ide.texteditor.TextLineNumber;
import main.com.toolbar.CompileButton;
import main.com.toolbar.OpenButton;
import main.com.toolbar.SaveButton;
import main.com.toolbar.listeners.CompileButtonListener;
import main.com.toolbar.listeners.OpenButtonListener;
import main.com.toolbar.listeners.PropertiesButtonListener;
import main.com.toolbar.listeners.SaveFileListener;

public class WorkBench extends JFrame {

	private static final long serialVersionUID = -8731589343197836723L;

	private JTextPane editor;
	private JTextArea buildlog;

	private JButton newFileButton;
	private CompileButton compilerButton;
	private SaveButton saveFileButton;
	private OpenButton openFileButton;
	
	// Custom style for editor environment
	private TextEditorDocument textarea;

	private Properties properties;

	public WorkBench() {

		// Change this after sharing
		super("Fuzzy-Runnable");
		
		properties = new Properties();	

		textarea = new TextEditorDocument();
		editor = new JTextPane(textarea);	
		buildlog = new JTextArea();

	}

	public void loadWorkbench() {	

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		// Initialize frame and add the menu items
		this.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize((int) width/2, (int)(height/1.2));
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setBackground(new Color(217, 217, 217));		

		// Load all components for the workbench
		buildToolbar();
		createMenuBar();
		loadConsoles();

		// Reveal the frame
		this.setVisible(true);
		this.validate();
		this.pack();
		editor.requestFocus();

	}
	
	private void createMenuBar() {

		JMenuBar menu = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenuItem open = new JMenuItem("Load Script");
		JMenuItem save = new JMenuItem("Save Script");
		JMenuItem exit = new JMenuItem("Exit");

		JMenu build = new JMenu("Build Tools");
		JMenuItem buildScript = new JMenuItem("Build Script");
		JMenuItem packageLoader = new JMenuItem("Set Package Imports");
		
		JMenu help = new JMenu("Help");
		JMenuItem javaDocHelp = new JMenuItem("View Javadoc");
		
		menu.setBackground(new Color(217,217,217));
		menu.add(file);
		file.add(open);
		file.add(save);
		file.add(exit);

		menu.add(build);
		build.add(buildScript);
		build.add(new CommandLineArgument(properties));
		build.add(packageLoader);	

		save.addActionListener(new MenuListener(1));
		open.addActionListener(new MenuListener(2));
		exit.addActionListener(new MenuListener(3));
		buildScript.addActionListener(new MenuListener(4));

		packageLoader.addActionListener(new PropertiesButtonListener(properties));

		buildScript.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		open.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));
		save.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));

		menu.add(help);
		help.add(javaDocHelp);
		
		javaDocHelp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get path to jar location
				String path  = "";
				try {
					path = WorkBench.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
				} catch (URISyntaxException e1) {
				
				}
				
				// Trim off the "Fuzzy-Runner.jar" portion
				// TODO: Fix when app is renamed!!!
				path = path.substring(0, path.length()-16);
				
				buildlog.append(path);
				
				// Launch the users default browser.
				if(Desktop.isDesktopSupported())
				{
					try {
						Desktop.getDesktop().browse(new URI("file://" + path + "doc/index.html"));
					} catch (IOException | URISyntaxException e1) {
						buildlog.append("Something went wrong looking for the path to your Javadocs.");
					}
				}
			}
		});
		
		this.setJMenuBar(menu);
	}

	private void loadConsoles() {

		Border b = new LineBorder(Color.LIGHT_GRAY, 1, true);

		Font font = new Font("Normal", Font.PLAIN, 14);
		font = font.deriveFont(Font.PLAIN, 14);

		JScrollPane mainScroll = new JScrollPane(editor);
		JScrollPane outputScroll = new JScrollPane(buildlog);

		GridBagConstraints constraint = new GridBagConstraints();

		// Create the script area
		editor.setEditable(true);
		editor.setFont(font);
		editor.setBorder(b);
		editor.setBackground(new Color(252, 252, 252));
		editor.addMouseListener(new RightClickListener());
		
		mainScroll.setPreferredSize(new Dimension(
				this.getWidth()-12, (int)(this.getHeight()/1.4)));
	
		mainScroll.setBorder(BorderFactory.createTitledBorder(
							 BorderFactory.createLineBorder(
									Color.LIGHT_GRAY,1,true), "Current Script:"));
		
		mainScroll.setBackground(new Color(217, 217, 217));

		TextLineNumber tln = new TextLineNumber(editor);
		mainScroll.setRowHeaderView(tln);
		
		constraint.gridx = 0;
		constraint.gridy = 1;
		constraint.fill = GridBagConstraints.BOTH;
		constraint.weightx = 1;
		constraint.weighty = .99;
		
		this.getContentPane().add(mainScroll, constraint);

		// To display build logs
		buildlog.setLineWrap(true);
		buildlog.setEditable(false);
		buildlog.setBorder(b);
		buildlog.setBackground(new Color(252, 252, 252));
		buildlog.setFont(font);
		
		outputScroll.setPreferredSize(new Dimension(
				getWidth()-12, (int)(getHeight()/5)));
		
		outputScroll.setBorder(BorderFactory.createTitledBorder(
							   BorderFactory.createLineBorder(
									   Color.LIGHT_GRAY,1,true), "Build Log:"));
		outputScroll.setBackground(new Color(217, 217, 217));
		
		constraint.gridx = 0;
		constraint.gridy = 2;
		constraint.fill = GridBagConstraints.BOTH;
		constraint.weightx = 1;
		constraint.weighty = .2;
		this.getContentPane().add(outputScroll, constraint);

	}

	private void buildToolbar() {
		
		GridBagConstraints constraint = new GridBagConstraints();

		JPanel toolbar = new JPanel();
		newFileButton = new JButton();
		openFileButton = new OpenButton();
		saveFileButton = new SaveButton();
		compilerButton = new CompileButton();

		toolbar.setPreferredSize(new Dimension((int)this.getWidth(), 25));
		toolbar.setMaximumSize(new Dimension((int)this.getWidth(), 25));
		toolbar.setLayout(null);
		toolbar.setBackground(new Color(217, 217, 217));

		// Setup listeners
		newFileButton.addActionListener(new NewFileListener());
		openFileButton.addActionListener(new OpenButtonListener(editor, properties));
		saveFileButton.addActionListener(new SaveFileListener(editor, properties));
		compilerButton.addActionListener(new CompileButtonListener(editor, buildlog, properties));

		Image img;
		// Empty catches are bad, mmmkkayyyy?
		try {
			img = ImageIO.read(getClass().getResource("/res/rsz_newfile.png"));
			newFileButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
			System.exit(-1);
		}
		newFileButton.setBounds(2, 0, 20, 20);
		newFileButton.setToolTipText("New File");
		toolbar.add(newFileButton);

		try {
			img = ImageIO.read(getClass().getResource("/res/rsz_save.png"));
			saveFileButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
			System.exit(-1);
		}
		saveFileButton.setBounds(22, 0, 20, 20);
		saveFileButton.setToolTipText("Save File As");
		toolbar.add(saveFileButton);

		try {
			img = ImageIO.read(getClass().getResource("/res/rsz_open.png"));
			openFileButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
			System.exit(-1);
		}
		openFileButton.setBounds(44, 0, 20, 20);
		openFileButton.setToolTipText("Open File");
		toolbar.add(openFileButton);

		try {
			img = ImageIO.read(getClass().getResource("/res/rsz_play.png"));
			compilerButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
			System.exit(-1);
		}
		compilerButton.setBounds(66, 0, 20, 20);
		compilerButton.setToolTipText("Compile Script");
		toolbar.add(compilerButton);	
		
		constraint.fill = GridBagConstraints.BOTH;
		constraint.gridx = 0;
		constraint.gridy = 0;
		constraint.weightx = 1;
		constraint.weighty = .1;
		
		toolbar.setLocation(0, 0);
		
		this.getContentPane().add(toolbar, constraint);

	}

	/**
	 * Main menu listener for the File options
	 * 
	 * @author Mike Nowicki
	 *
	 */
	private class MenuListener implements ActionListener {

		private int listenerType;

		public MenuListener(int id) { 
			this.listenerType = id;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// Save file
			if (listenerType == 1) {

				saveFileButton.doClick();
				textarea.isSaved();

			} else if (listenerType == 2) {
				// Open file, check to save first
				if (textarea.documentHasChanged()) {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog (null,
							"Would You Like to Save your Work?","Warning",dialogButton);
					if(dialogResult == JOptionPane.YES_OPTION){
						saveFileButton.doClick();
						textarea.isSaved();
					}
				}

				openFileButton.doClick();
				textarea.isSaved();
			} else if (listenerType == 3) {
				// Exit application, check if saved first				
				int dialogButton;
				int dialogResult = JOptionPane.NO_OPTION;

				if (textarea.documentHasChanged()) {
					dialogButton = JOptionPane.YES_NO_OPTION;
					dialogResult = JOptionPane.showConfirmDialog (null,
							"Would You Like to Save your Work?","Warning",dialogButton);
					if(dialogResult == JOptionPane.YES_OPTION){
						saveFileButton.doClick();
						textarea.isSaved();
					}
				}
				// If the document has not changed, and the user does not click yes then the app
				// can exit
				if (!textarea.documentHasChanged() || dialogResult == JOptionPane.NO_OPTION) {
					System.exit(0);
				}
				// Compile script
			} else if (listenerType == 4) {
				compilerButton.doClick();
			}
		}
	}

	/**
	 * Trivial listener to create a new file
	 * 
	 * @author Mike Nowicki
	 *
	 */
	private class NewFileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent click) {

			if (textarea.documentHasChanged()) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, 
						"Would You Like to Save your Work?","Warning", dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					saveFileButton.doClick();
					textarea.isSaved();
				}
			}

			editor.setText("");
			saveFileButton.removeActionListener(saveFileButton.getActionListeners()[0]);
			saveFileButton.addActionListener(new SaveFileListener(editor, properties));
			properties.clearArguments();
			properties.clearImports();
		}
	}
}
