package main.com.ide;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
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
import javax.swing.border.TitledBorder;

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

	/**
	 * Where the user enters their script
	 */
	private JTextPane editor;
	/**
	 * TextArea displays error messages or scirpt output
	 */
	private JTextArea buildlog;
	/**
	 * Line Numbering for the editor text pane
	 */
	private TextLineNumber tln;
	/*
	 * The two scroll panes that will contain the editor and build log
	 */
	private JScrollPane mainScroll;
	private JScrollPane outputScroll;
	
	/**
	 * Panel that will hold all the buttons available to the user
	 */
	private JPanel toolbar;
	
	/*
	 * Collection of buttons that will be on the toolbar
	 */
	private JButton newFileButton;
	private CompileButton compilerButton;
	private SaveButton saveFileButton;
	private OpenButton openFileButton;

	// Custom style for editor environment
	private TextEditorDocument textarea;

	// Properties of script for compiler
	private Properties properties;
	
	// Settings for the editors theme
	private ThemeSettings settings;

	public WorkBench() {
		
		super("Fuzzy-Runner");

		properties = new Properties();

		settings = new ThemeSettings();

		textarea = new TextEditorDocument();
		editor = new JTextPane(textarea);	
		buildlog = new JTextArea();

	}


	/**
	 * Main method to load all components on the frame
	 */
	public void loadWorkbench() {	
		
		// Read settings file to determine how to decorate the editor
		settings.loadEnvironmentSettings();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();

		// Initialize frame and add the menu items
		this.setLayout(new GridBagLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize((int) width/2, (int)(height/1.2));
		this.setResizable(true);
		this.setLocationRelativeTo(null);	

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

	/**
	 *  Create all menu bar menus and items
	 */
	private void createMenuBar() {

		JMenuBar menu = new JMenuBar();

		JMenu file = new JMenu("File");
		JMenuItem open = new JMenuItem("Load Script");
		JMenuItem save = new JMenuItem("Save Script");
		JMenuItem exit = new JMenuItem("Exit");

		JMenu build = new JMenu("Build Tools");
		JMenuItem buildScript = new JMenuItem("Build Script");
		JMenuItem packageLoader = new JMenuItem("Set Package Imports");

		JMenu options = new JMenu("Options");
		JMenuItem editorThemes = new JMenuItem("Themes");

		JMenu help = new JMenu("Help");
		JMenuItem javaDocHelp = new JMenuItem("View Library Javadoc");
		JMenuItem appHelp = new JMenuItem("General Help");

		menu.setBackground(new Color(217, 217, 217));
		menu.add(file);
		file.add(open);
		file.add(save);
		file.add(exit);

		menu.add(build);
		build.add(buildScript);
		build.add(packageLoader);	
		build.add(new CommandLineArgument(properties));

		save.addActionListener(new MenuListener(1));
		open.addActionListener(new MenuListener(2));
		exit.addActionListener(new MenuListener(3));
		buildScript.addActionListener(new MenuListener(4));

		packageLoader.addActionListener(new PropertiesButtonListener(properties));

		buildScript.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		open.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));
		save.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));

		menu.add(options);
		options.add(editorThemes);

		// When clicked create the new frame to pick the theme
		editorThemes.addActionListener(new ThemeListener());

		menu.add(help);
		help.add(appHelp);
		help.add(javaDocHelp);

		/**
		 * This listener finds the path to the JavaDocs for the supplied
		 * libraries, and attempts to launch them in the users default
		 * browser
		 */
		javaDocHelp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Get path to jar location
				String path  = "";
				try {
					// I don't think there's a way to reduce how grossly long this is...
					path = WorkBench.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
				} catch (URISyntaxException e1) {

				}

				/*
				 * Important, this will not work in Eclipse or an IDE, 
				 * it is for the built version ONLY!!
				 */
				// this has to change if being run in eclipse
				path = path.substring(0, path.length()-11);

				// Launch the users default browser.
				if(Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(new URI("file://" + 
								path + 
								"doc/index.html"));
					} catch (IOException | URISyntaxException e1) {
						buildlog.append("Could not find the path to your Javadocs.");
					}
				}
			}
		});

		appHelp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: Implement help function
			}
		});

		this.setJMenuBar(menu);
	}

	// Create editor environment and console
	private void loadConsoles() {

		Border b = new LineBorder(Color.LIGHT_GRAY, 1, true);

		Font font = new Font("Normal", Font.PLAIN, 14);
		font = font.deriveFont(Font.PLAIN, 14);

		mainScroll = new JScrollPane(editor);
		outputScroll = new JScrollPane(buildlog);

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
				BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
				"Script:", 
				TitledBorder.LEFT, 
				TitledBorder.LEFT, 
				new Font("Normal", Font.PLAIN, 12), 
				settings.getBuildLogColour()));

		mainScroll.setBackground(settings.getEnvironmentColour());

		tln = new TextLineNumber(editor);
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
				BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
				"Build Log:", 
				TitledBorder.LEFT, 
				TitledBorder.LEFT, 
				new Font("Normal", Font.PLAIN, 12), 
				settings.getBuildLogColour()));
		
		outputScroll.setBackground(settings.getEnvironmentColour());

		constraint.gridx = 0;
		constraint.gridy = 2;
		constraint.fill = GridBagConstraints.BOTH;
		constraint.weightx = 1;
		constraint.weighty = .2;
		this.getContentPane().add(outputScroll, constraint);

	}

	// Create and place all buttons on the toolbar
	private void buildToolbar() {

		GridBagConstraints constraint = new GridBagConstraints();

		toolbar = new JPanel();
		newFileButton = new JButton();
		openFileButton = new OpenButton();
		saveFileButton = new SaveButton();
		compilerButton = new CompileButton();

		toolbar.setPreferredSize(new Dimension((int)this.getWidth(), 30));
		toolbar.setMaximumSize(new Dimension((int)this.getWidth(), 30));
		toolbar.setLayout(null);
		toolbar.setBackground(new Color(217, 217, 217));

		// Setup listeners
		newFileButton.addActionListener(new NewFileListener());
		openFileButton.addActionListener(new OpenButtonListener(
				editor, properties));
		saveFileButton.addActionListener(new SaveFileListener(
				editor, properties));
		compilerButton.addActionListener(new CompileButtonListener(
				editor, buildlog, properties));

		/*
		 * Load all images for the buttons, place each button on the toolbar
		 */
		
		Image img = null;
		
		try {
			img = ImageIO.read(getClass().getResource("/res/rsz_newfile.png"));
			newFileButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
			System.exit(-1);
		}
		newFileButton.setBounds(2, 0, 30, 30);
		newFileButton.setToolTipText("New File");
		toolbar.add(newFileButton);

		try {
			img = ImageIO.read(getClass().getResource("/res/rsz_save.png"));
			saveFileButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
			System.exit(-1);
		}
		saveFileButton.setBounds(32, 0, 30, 30);
		saveFileButton.setToolTipText("Save File As");
		toolbar.add(saveFileButton);

		try {
			img = ImageIO.read(getClass().getResource("/res/rsz_open.png"));
			openFileButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
			System.exit(-1);
		}
		openFileButton.setBounds(64, 0, 30, 30);
		openFileButton.setToolTipText("Open File");
		toolbar.add(openFileButton);

		try {
			img = ImageIO.read(getClass().getResource("/res/rsz_play.png"));
			compilerButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
			System.exit(-1);
		}
		compilerButton.setBounds(96, 0, 30, 30);
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
	 * Overriding method that paints the frame, updates the themes
	 * 
	 * @param g The graphics for the component.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		// Update environment theme
		mainScroll.setBackground(settings.getEnvironmentColour());
		outputScroll.setBackground(settings.getEnvironmentColour());
		
		mainScroll.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
				"Script:", 
				TitledBorder.LEFT, 
				TitledBorder.LEFT, 
				new Font("Normal", Font.PLAIN, 12), 
				settings.getBuildLogColour()));
		
		outputScroll.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
				"Build Log:", 
				TitledBorder.LEFT, 
				TitledBorder.LEFT, 
				new Font("Normal", Font.PLAIN, 12), 
				settings.getBuildLogColour()));
		
		// Update text colouring for editor
		textarea.setQuotations(settings.getQuotations());
		textarea.setReservedWords(settings.getReservedWords());
		textarea.setComments(settings.getComments());
		textarea.setDefaultColour(settings.getDefaultColour());
		
		editor.setText(editor.getText());
		
		// Update line number colouring
		tln.setBackground(settings.getEditorColour());
		tln.setForeground(settings.getLineNumberColour());
		
		// Update the background colour
		editor.setBackground(settings.getEditorColour());
		
		// Update the build log colours
		buildlog.setBackground(settings.getEditorColour());
		buildlog.setForeground(settings.getBuildLogColour());
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
					int dialogResult = JOptionPane.showConfirmDialog(
							null,
							"Would You Like to Save your Work?",
							"Warning",
							dialogButton);
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
					dialogResult = JOptionPane.showConfirmDialog(
							null,
							"Would You Like to Save your Work?",
							"Warning",
							dialogButton);
					if(dialogResult == JOptionPane.YES_OPTION){
						saveFileButton.doClick();
						textarea.isSaved();
					}
				}
				// If the document has not changed, and the user does 
				// not click yes then the program can exit
				if (!textarea.documentHasChanged() || 
						dialogResult == JOptionPane.NO_OPTION) {
					System.exit(0);
				}
				// Compile script
			} else if (listenerType == 4) {
				compilerButton.doClick();
			}
		}
	}
	
	
	/**
	 * Trivial listener to create a new file, clears
	 * the text area and starts new.
	 * 
	 * @author Mike Nowicki
	 *
	 */
	private class NewFileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent click) {

			// This checks if modifications have been made,
			// if so prompt the user to save their work.
			if (textarea.documentHasChanged()) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(null, 
						"Would You Like to Save your Work?","Warning", dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					saveFileButton.doClick();
					textarea.isSaved();
				}
			}

			editor.setText("");
			// Must remove action listeners to prevent redundant behaviour,
			// since only one exists it must be at array index 0
			saveFileButton.removeActionListener(saveFileButton.getActionListeners()[0]);
			saveFileButton.addActionListener(new SaveFileListener(editor, properties));
			properties.clearArguments();
			properties.clearImports();
		}
	}
	
	/**
	 * ActionListener that creates the frame that allows
	 * the user to pick which colour theme they would
	 * like to use.
	 * 
	 * @author Michael Nowicki
	 *
	 */
	private class ThemeListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			Runnable task = new Runnable(){
				@Override
				public void run() {
					new ThemePicker(settings, new ApplyListener());
				}
			};
			EventQueue.invokeLater(task);
		}
	}
	
	/**
	 * Listener passed to ThemePicker to update environment settings
	 * and repaints the component when it performs its' action
	 * 
	 * @author Michael  Nowicki
	 *
	 */
	private class ApplyListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			settings.updateEnvironmentSettings();
			repaint();
		}
	}
}
