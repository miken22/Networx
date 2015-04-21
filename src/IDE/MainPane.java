package IDE;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

import BuildTools.MainCompiler;

public class MainPane {

	// Main frame components
	private JFrame frame;
	private Container mainContainer;
	private JScrollPane mainScroll;
	private JScrollPane outputScroll;
	private JTextPane worksheet;
	private JTextArea output;

	private StyledDocument textarea;
	private Style workStyle;
	private Font font;

	private JMenuBar menu;
	private JMenu file;
	private JMenu build;
	private JMenuItem exit;
	private JMenuItem save;
	private JMenuItem open;
	private JMenuItem buildScript;

	public void loadWorkbench() {
		worksheet = new JTextPane();
		output = new JTextArea();
		mainScroll = new JScrollPane(worksheet);
		outputScroll = new JScrollPane(output);

		workStyle = worksheet.addStyle("workStyle", null);

		menu = new JMenuBar();
		file = new JMenu("File");
		open = new JMenuItem("Load Script");
		save = new JMenuItem("Save Script");
		exit = new JMenuItem("Exit");
		
		build = new JMenu("Build Tools");
		buildScript = new JMenuItem("Build Script");

		save.addActionListener(new MenuListener(1));
		open.addActionListener(new MenuListener(2));
		exit.addActionListener(new MenuListener(3));
		
		buildScript.addActionListener(new MenuListener(4));

		createFrame();

	}

	private void createFrame() {

		Border b = new LineBorder(Color.LIGHT_GRAY, 1, true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();

		// Initialize frame and add the menu items
		frame = new JFrame("Networx Graph Editor");
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize((int) width, (int) height);
		frame.setResizable(true);
		frame.setJMenuBar(menu);

		menu.setBackground(new Color(244,244,244));
		menu.add(file);
		file.add(open);
		file.add(save);
		file.add(exit);
		
		menu.add(build);
		build.add(buildScript);
		
		mainContainer = frame.getContentPane();
		mainContainer.setBackground(new Color(240, 240, 240));

		// Main font theme
		try {
			font = Font.createFont(0, this.getClass().getResourceAsStream("/Trebuchet MS.ttf"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create the script area
		worksheet.setEditable(true);
		worksheet.setContentType("text/html");
		font = font.deriveFont(Font.PLAIN, 14);
		worksheet.setFont(font);
		worksheet.setBorder(b);
		worksheet.setBackground(new Color(252, 252, 252));
		mainScroll.setBounds(2, 4, frame.getWidth() - (int)(width/4),  (int)(height/1.4));
		mainScroll.setBackground(new Color(240, 240, 240));
		mainScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1,true), "Current Script:"));
		mainContainer.add(mainScroll);

		// To display build logs
		output.setLineWrap(true);
		output.setEditable(false);
		output.setBorder(b);
		output.setBackground(new Color(252, 252, 252));
		output.setFont(font);
		outputScroll.setBounds(4, (int)(height/1.4), frame.getWidth() - (int)(width/4), frame.getHeight() - (int)(height/1.28));
		outputScroll.setBackground(new Color(240, 240, 240));
		outputScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1,true), "Build Log:"));
		
		mainContainer.add(outputScroll);

		// Reveal the frame
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		worksheet.requestFocus();

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
				String name = (String) JOptionPane.showInputDialog(null,
						"Save As:\n", "Set File Name",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				try {
					SaveText.saveWorksheet(name, worksheet.getText());
				} catch (FileNotFoundException e) {
					System.exit(0);
				}
			} else if (listenerType == 2) {
				// TODO: Load script into worksheet
				
			} else if (listenerType == 3) {
				System.exit(0);
			} else if (listenerType == 4) {
				
				MainCompiler compiler = new MainCompiler();
				compiler.buildScript(worksheet.getText());
				
			}
		}

	}
}
