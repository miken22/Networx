package ide;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;

import buildtools.CompileButtonListener;
import buildtools.OpenButtonListener;
import buildtools.PropertiesButtonListener;
import buildtools.SaveFileListener;

public class MainPane {

	// Main frame components
	private JFrame frame;
	private Container mainContainer;
	private JScrollPane mainScroll;
	private JScrollPane outputScroll;
	private JTextPane worksheet;
	private JTextArea output;

	private JPanel toolbar;
	private JButton newFile;
	private JButton compiler;
	private JButton saveFile;
	private JButton openFile;

	// Custom style for editor environment
	private TextEditorDocument textarea;
	private Font font;

	private JMenuBar menu;
	private JMenu file;
	private JMenu build;
	private JMenuItem exit;
	private JMenuItem save;
	private JMenuItem open;
	private JMenuItem buildScript;
	private JMenuItem jungPackageLoader;
	private JMenuItem javaPackageLoader;

	private Properties properties;
	
	private String fileName = "";

	/**
	 * Main method to call to load the application frame
	 */
	public void loadWorkbench() {
			    
		properties = new Properties();
		
		frame = new JFrame("Networx Graph Editor");
		
		textarea = new TextEditorDocument();
		worksheet = new JTextPane(textarea);
		output = new JTextArea();
		mainScroll = new JScrollPane(worksheet);
		outputScroll = new JScrollPane(output);

		menu = new JMenuBar();
		file = new JMenu("File");
		open = new JMenuItem("Load Script");
		save = new JMenuItem("Save Script");
		exit = new JMenuItem("Exit");

		build = new JMenu("Build Tools");
		buildScript = new JMenuItem("Build Script");
		javaPackageLoader = new JMenuItem("Add Java Imports");
		jungPackageLoader = new JMenuItem("Add JUNG2 Packages");

		save.addActionListener(new MenuListener(1));
		open.addActionListener(new MenuListener(2));
		exit.addActionListener(new MenuListener(3));
		buildScript.addActionListener(new MenuListener(4));
		javaPackageLoader.addActionListener(new PropertiesButtonListener(properties, 0));
		jungPackageLoader.addActionListener(new PropertiesButtonListener(properties, 1));
		createFrame();
	}

	private void createFrame() {

		Border b = new LineBorder(Color.LIGHT_GRAY, 1, true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();

		// Initialize frame and add the menu items
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize((int) width/2, (int)(height/1.2));
//		frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		frame.setResizable(false);
		frame.setJMenuBar(menu);

		menu.setBackground(new Color(217,217,217));
		menu.add(file);
		file.add(open);
		file.add(save);
		file.add(exit);

		menu.add(build);
		build.add(buildScript);
		build.add(javaPackageLoader);
		build.add(jungPackageLoader);

		mainContainer = frame.getContentPane();
		mainContainer.setBackground(new Color(240, 240, 240));

		// Main font theme
		try {
			font = Font.createFont(0, this.getClass().getResourceAsStream("/resources/Trebuchet MS.ttf"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create the script area
		worksheet.setEditable(true);
		worksheet.addKeyListener(new KeyStrokeListener());
		font = font.deriveFont(Font.PLAIN, 14);
		worksheet.setFont(font);
		worksheet.setBorder(b);
		worksheet.setBackground(new Color(252, 252, 252));
		mainScroll.setBounds(2, 25, frame.getWidth()-12,  (int)(frame.getHeight()/1.4));
		mainScroll.setBackground(new Color(240, 240, 240));
		mainScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1,true), "Current Script:"));
		mainScroll.setBackground(new Color(217, 217, 217));
		mainContainer.add(mainScroll);

		// To display build logs
		output.setLineWrap(true);
		output.setEditable(false);
		output.setBorder(b);
		output.setBackground(new Color(252, 252, 252));
		output.setFont(font);
		outputScroll.setBounds(2, (mainScroll.getHeight()+25), (frame.getWidth() - 12), (int)(mainScroll.getHeight()/4));
		outputScroll.setBackground(new Color(240, 240, 240));
		outputScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1,true), "Build Log:"));
		outputScroll.setBackground(new Color(217, 217, 217));
		mainContainer.add(outputScroll);

		buildToolbar();

		mainContainer.setBackground(new Color(217, 217, 217));
		// Reveal the frame
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		worksheet.requestFocus();

	}

	private void buildToolbar() {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();

		toolbar = new JPanel();
		newFile = new JButton();
		openFile = new JButton();
		saveFile = new JButton();
		compiler = new JButton();

		toolbar.setBounds(0, 0, (int)width, 25);
		toolbar.setLayout(null);
		toolbar.setBackground(new Color(217, 217, 217));

		// Setup compiler listener
		newFile.addActionListener(new NewFileListener());
		openFile.addActionListener(new OpenButtonListener(worksheet));
		saveFile.addActionListener(new SaveFileListener(worksheet));
		compiler.addActionListener(new CompileButtonListener(worksheet, output, properties));

		try {
			Image img = ImageIO.read(getClass().getResource("/resources/rsz_newfile.png"));
			newFile.setIcon(new ImageIcon(img));
		} catch (IOException ex) {

		}
		newFile.setBounds(2, 0, 20, 20);
		newFile.setToolTipText("New File");
		toolbar.add(newFile);

		try {
			Image img = ImageIO.read(getClass().getResource("/resources/rsz_save.png"));
			saveFile.setIcon(new ImageIcon(img));
		} catch (IOException ex) {

		}
		saveFile.setBounds(22, 0, 20, 20);
		saveFile.setToolTipText("Save File As");
		toolbar.add(saveFile);

		try {
			Image img = ImageIO.read(getClass().getResource("/resources/rsz_open.png"));
			openFile.setIcon(new ImageIcon(img));
		} catch (IOException ex) {

		}
		openFile.setBounds(44, 0, 20, 20);
		openFile.setToolTipText("Open File");
		toolbar.add(openFile);

		try {
			Image img = ImageIO.read(getClass().getResource("/resources/rsz_play.png"));
			compiler.setIcon(new ImageIcon(img));
		} catch (IOException ex) {

		}
		compiler.setBounds(66, 0, 20, 20);
		compiler.setToolTipText("Compile Script");
		toolbar.add(compiler);		
		mainContainer.add(toolbar);

	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	/**
	 * Main menu listener for the File options
	 * 
	 * @author Michael
	 *
	 */
	public class MenuListener implements ActionListener {

		private int listenerType;

		// TODO: Make a variable for the pane, pass instance of "this" in to the constructor
		// and move this class into it's own file.

		public MenuListener(int id) {
			this.listenerType = id;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (listenerType == 1) {
				saveFile.doClick();
			} else if (listenerType == 2) {
				openFile.doClick();
			} else if (listenerType == 3) {
				System.exit(0);
			} else if (listenerType == 4) {
				compiler.doClick();
			}
		}

	}
	/**
	 * Trivial listener to create a new file
	 * 
	 * @author Michael
	 *
	 */
	public class NewFileListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent click) {
			worksheet.setText("");
		}
	}

	/**
	 * Trivial key listener to insert four spaces when the tab key
	 * is pressed
	 * 
	 * @author Michael
	 *
	 */
	private class KeyStrokeListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent key) {
			if (key.getKeyCode() == KeyEvent.VK_TAB) {
				try {
					textarea.insertString(worksheet.getCaretPosition(), "    ", null);
					key.consume();
					return;
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {

		}

		@Override
		public void keyTyped(KeyEvent arg0) {

		}

	}
}
