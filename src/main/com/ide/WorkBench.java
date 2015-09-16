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
import java.net.URL;

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
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import main.com.ide.mouse.RightClickListener;
import main.com.ide.packages.PropertyMenuItem;
import main.com.ide.texteditor.TextEditor;
import main.com.ide.texteditor.TextEditorDocument;
import main.com.ide.texteditor.TextLineNumber;
import main.com.toolbar.CompileButton;
import main.com.toolbar.OpenButton;
import main.com.toolbar.SaveButton;

public class WorkBench extends JFrame {

	private static final long serialVersionUID = -8731589343197836723L;

	/**
	 * Where the user enters their script
	 */
//	private JTextPane editor;
	private TextEditor editor;
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

		super("Grapher");

		properties = new Properties();

		settings = new ThemeSettings();

		textarea = new TextEditorDocument();
		editor = new TextEditor(textarea);	
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
		// This is disabled because resizing causes weird side effects.
		this.setResizable(false);
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
		PropertyMenuItem packageLoader = new PropertyMenuItem(properties,
														"Set Package Imports");

		JMenu options = new JMenu("Preferences");
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

		// Implement each listener for the menu items below
		
		save.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// Perform the save action 
				saveFileButton.doClick();
				textarea.isSaved();
			}
		});
		
		open.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// Check if the document was modifed, prompt the
				// user to save if it has been.
				if (textarea.documentHasChanged()) {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(
							null,
							"Would You Like to Save your Work?",
							"Warning",
							dialogButton);
					// If the say yes save it, otherwise move on and overwrite
					if(dialogResult == JOptionPane.YES_OPTION){
						saveFileButton.doClick();
						textarea.isSaved();
					}
				}
			}
		});
		
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// Style for the dialog box
				int dialogButton;
				// Assume no, save if they click yes
				int dialogResult = JOptionPane.NO_OPTION;
				// If it has changed ask the user to save before exiting.
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
						dialogResult != JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		
		buildScript.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				compilerButton.doClick();	// Run the "compiler"
			}
		});

		// Set hotkeys which are displayed in the menu
		buildScript.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		open.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));
		save.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));

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
					URL url = WorkBench.class.getProtectionDomain().getCodeSource().getLocation();
					path = url.toURI().getPath();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
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
				// TODO: Implement help function similar to above
			}
		});

		this.setJMenuBar(menu);
	}

	// Create editor environment and console
	private void loadConsoles() {

		Border border = new LineBorder(Color.LIGHT_GRAY, 1, true);

		Font font = new Font(Font.MONOSPACED, 
							 Font.PLAIN,
							 settings.getFontSize());

		mainScroll = new JScrollPane(editor);
		outputScroll = new JScrollPane(buildlog);

		GridBagConstraints constraint = new GridBagConstraints();

		// Create the script area
		editor.setEditable(true);
		editor.setFont(font);
		editor.setBorder(border);
		editor.setBackground(new Color(252, 252, 252));
		editor.addMouseListener(new RightClickListener());

		// Set the parameters for the scrollpage holding the editor
		mainScroll.setPreferredSize(new Dimension(
				this.getWidth()-12, (int)(this.getHeight()/1.4)));

		mainScroll.setBorder(BorderFactory.createTitledBorder(
				border,
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
		buildlog.setBorder(border);
		buildlog.setBackground(new Color(252, 252, 252));
		buildlog.setFont(font);

		// Set the parameters for the scrollpane holding the buildlog
		outputScroll.setPreferredSize(new Dimension(
				getWidth()-12, (int)(getHeight()/5)));

		outputScroll.setBorder(BorderFactory.createTitledBorder(
				border,
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
		
		this.add(outputScroll, constraint);

	}

	// Create and place all buttons on the toolbar
	private void buildToolbar() {

		toolbar = new JPanel();
		newFileButton = new JButton();
		openFileButton = new OpenButton(editor, properties);
		saveFileButton = new SaveButton(editor, properties);
		compilerButton = new CompileButton(editor, buildlog, properties);

		toolbar.setPreferredSize(new Dimension((int)this.getWidth(), 30));
		toolbar.setMaximumSize(new Dimension((int)this.getWidth(), 30));
		toolbar.setLayout(null);
		toolbar.setBackground(new Color(217, 217, 217));

		// Setup listener for new file
		newFileButton.addActionListener(new NewFileListener());

		/*
		 * Load all images for the buttons, place each button on the toolbar
		 */

		Image img = null;

		// HTML Formatting used on tooltip text to enable multiline
		// support to indicate hotkeys.
		
		try {
			img = ImageIO.read(getClass().getResource("/res/rsz_newfile.png"));
			newFileButton.setIcon(new ImageIcon(img));
			newFileButton.setBounds(2, 0, 30, 30);
			newFileButton.setBackground(new Color(252,252,252));
			newFileButton.setToolTipText("<html>New File <br>(Ctrl + N)</html>");
			toolbar.add(newFileButton);

			img = ImageIO.read(getClass().getResource("/res/rsz_save.png"));
			saveFileButton.setIcon(new ImageIcon(img));
			saveFileButton.setBounds(32, 0, 30, 30);
			saveFileButton.setBackground(new Color(252,252,252));
			saveFileButton.setToolTipText("<html>Save File As <br>(Ctrl + S)</html>");
			toolbar.add(saveFileButton);

			img = ImageIO.read(getClass().getResource("/res/rsz_open.png"));
			openFileButton.setIcon(new ImageIcon(img));
			openFileButton.setBounds(64, 0, 30, 30);
			openFileButton.setBackground(new Color(252,252,252));
			openFileButton.setToolTipText("<html>Open File <br>(Ctrl + O)</html>");
			toolbar.add(openFileButton);


			img = ImageIO.read(getClass().getResource("/res/rsz_play.png"));
			compilerButton.setIcon(new ImageIcon(img));
			compilerButton.setBounds(96, 0, 30, 30);
			compilerButton.setBackground(new Color(252,252,252));
			compilerButton.setToolTipText("<html>Compile Script <br>(F5)</html>");
			toolbar.add(compilerButton);	

		} catch (Exception ex) {
			// If something fails to load have the user verify the directory is there
			// and exit the application.
			JOptionPane.showMessageDialog(
					null,
					"Error loading resources, ensure the lib folder is "+
					"in your install directory.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}

		// Set constraints for layout of toolbar, set location and
		// add to the workbench frame.
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.BOTH;
		constraint.gridx = 0;
		constraint.gridy = 0;
		constraint.weightx = 1;
		constraint.weighty = .1;

		toolbar.setLocation(0, 0);

		this.add(toolbar, constraint);

	}
	
	/**
	 * Overriding method that paints the frame, updates the themes
	 * according to what was specified in the settings
	 * 
	 * @param g The graphics for the component.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Border border = new LineBorder(Color.LIGHT_GRAY, 1, true);

		// Update environment theme
		mainScroll.setBackground(settings.getEnvironmentColour());
		outputScroll.setBackground(settings.getEnvironmentColour());

		mainScroll.setBorder(BorderFactory.createTitledBorder(
				border,
				"Script:", 
				TitledBorder.LEFT, 
				TitledBorder.LEFT, 
				new Font("Normal", Font.PLAIN, 12), 
				settings.getBuildLogColour()));

		outputScroll.setBorder(BorderFactory.createTitledBorder(
				border,
				"Build Log:", 
				TitledBorder.LEFT, 
				TitledBorder.LEFT, 
				new Font("Normal", Font.PLAIN, 12), 
				settings.getBuildLogColour()));
		

		Font font = new Font(Font.MONOSPACED, 
							 Font.PLAIN,
							 settings.getFontSize());
		
		editor.setFont(font);

		// Update text colouring for editor
		textarea.setQuotations(settings.getQuotations());
		textarea.setReservedWords(settings.getReservedWords());
		textarea.setComments(settings.getComments());
		textarea.setDefaultColour(settings.getDefaultColour());

		editor.setText(editor.getText());

		// Update line number colouring
		tln.setBackground(settings.getEditorColour());
		tln.setForeground(settings.getLineNumberColour());
		tln.setFont(font);

		// Update the background colour
		editor.setBackground(settings.getEditorColour());

		// Update the build log colours
		buildlog.setBackground(settings.getEditorColour());
		buildlog.setForeground(settings.getBuildLogColour());
		buildlog.setFont(font);
		
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
			
			// Clear properties for new script
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
			// Update settings file before repainting the frame
			settings.updateEnvironmentSettings();
			repaint();
		}
	}
	
	/**
	 * Main entry point of the application.
	 * 
	 * @param args Program arguments
	 */
	public static void main(String[] args){		
		// Create a new runtime instance and have the swing event queue
		// initialize the application.
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				
				String OS = System.getProperty("os.name");
				// Mac is not supported, exit program
				if (OS.indexOf("mac") >= 0) {
					JOptionPane.showMessageDialog(null,
							"Sorry, only Windows and Linux currently supported.");
					System.exit(-1);			
				}
				// Launch the program
				WorkBench pane = new WorkBench();
			    pane.loadWorkbench();
			}
		}); 
	
	}	
}
