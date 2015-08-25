package main.com.ide;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import main.com.ide.mouse.RightClickListener;
import main.com.ide.texteditor.TextEditorDocument;
import main.com.ide.texteditor.TextLineNumber;

/**
 * Class that allows the user to choose the editor theme.
 * 
 * It currently does not override the main window theme.
 * 
 * @author Mike Nowicki  
 *
 */
public class ThemePicker extends JFrame{

	private static final long serialVersionUID = 1L;

	private ThemeSettings themeSettings;
	
	private TextEditorDocument textarea = new TextEditorDocument();
	private JTextPane editor = new JTextPane(textarea);
	
	private final String textString = "// This is a comment \n\r" +
									  "public static void main(String[] args) { \r\n" +
									  "    System.out.println(\"Hello World\");\r\n" +
									  "}";

	public ThemePicker() {
		themeSettings = new ThemeSettings();
		
		createFrame();
	}
		
	private void createFrame() {
		
		this.setLayout(new GridBagLayout());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		
		GridBagConstraints constraint = new GridBagConstraints();
		
		// Create radio panel and button to apply settings
		JPanel radioPanel = new JPanel();
		ButtonGroup group = new ButtonGroup();
		JRadioButton defaultSetting = new JRadioButton("Default");
		JRadioButton darkSetting = new JRadioButton("Dark");
		
		defaultSetting.setActionCommand("default");
		darkSetting.setActionCommand("dark");
		
		group.add(defaultSetting);
		group.add(darkSetting);
		
		JButton applyButton = new JButton("Apply");
		
		applyButton.addActionListener(new ApplyListener());
		radioPanel.setLayout(new GridLayout(1,3));
		
		radioPanel.add(defaultSetting);
		radioPanel.add(darkSetting);
		radioPanel.add(applyButton);
		
		defaultSetting.setSelected(true);
		
		ThemeListener listener = new ThemeListener(themeSettings);
		defaultSetting.addActionListener(listener);
		darkSetting.addActionListener(listener);
		
		constraint.gridx = 0;
		constraint.gridy = 0;
		constraint.fill = GridBagConstraints.BOTH;
		constraint.weightx = 1;
		constraint.weighty = 1;
		
		this.add(radioPanel);
		
		// Create editor window
		JScrollPane mainScroll = new JScrollPane(editor);
		
		Font font = new Font("Normal", Font.PLAIN, 14);
		font = font.deriveFont(Font.PLAIN, 14);

		// Create the script area
		editor.setEditable(false);
		editor.setFont(font);

		editor.setBackground(themeSettings.getBackgroundColour());
		editor.addMouseListener(new RightClickListener());
		
		mainScroll.setPreferredSize(new Dimension(350, 300));	
		mainScroll.setBackground(new Color(217, 217, 217));

		TextLineNumber tln = new TextLineNumber(editor);
		tln.setBackground(themeSettings.getBackgroundColour());
		mainScroll.setRowHeaderView(tln);
		
		editor.setText(textString);
		
		constraint.gridx = 0;
		constraint.gridy = 1;
		constraint.fill = GridBagConstraints.BOTH;
		constraint.weightx = 1;
		constraint.weighty = 1;
		
		this.getContentPane().add(mainScroll, constraint);
		
		this.validate();
		this.pack();
		
		this.setVisible(true);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		this.revalidate();
		this.repaint();
		
	}

	private class ThemeListener implements ActionListener {

		ThemeSettings settings;
		
		public ThemeListener(ThemeSettings themeSettings) {
			this.settings = themeSettings;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
	
			if (e.getActionCommand().equals("default")) {
				settings.setDefaultTheme();
			} else {
				settings.setDarkTheme();				
			}
			
			textarea.setQuotations(themeSettings.getQuotations());
			textarea.setReservedWords(themeSettings.getReservedWords());
			textarea.setComments(themeSettings.getComments());
			textarea.setDefaultColour(themeSettings.getDefaultColour());

			editor.setBackground(themeSettings.getBackgroundColour());
			editor.setText(textString);
			
			repaint();
			revalidate();
		}
	}

	private class ApplyListener implements ActionListener {
		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO: Have effect the actual environment
		}
	}
	
}
