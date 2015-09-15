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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import main.com.ide.texteditor.TextEditor;
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

	// Internal settings for updating the example text pane
	private ThemeSettings themeSettings;
	
	private TextLineNumber tln;
	
	private TextEditorDocument textarea = new TextEditorDocument();
	private TextEditor editor = new TextEditor(textarea);
	
	private ActionListener applyListener;
	
	private final String textString = "// This is a comment \n\r" +
									  "public static void main(String[] args) { \r\n" +
									  "    System.out.println(\"Hello World\");\r\n" +
									  "}\r\n";

	public ThemePicker(ThemeSettings themeSettings, ActionListener applyListener) {
		this.themeSettings = themeSettings;		
		this.applyListener = applyListener;
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
		
		// Applies the settings to the main JFrame, and
		// the second disposes this frame
		applyButton.addActionListener(applyListener);
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		applyButton.setBackground(new Color(252, 252, 252));
		
		// Create the panel for the radio buttons and place each
		// component
		radioPanel.setLayout(new GridLayout(1,3));
		radioPanel.add(defaultSetting);
		radioPanel.add(darkSetting);
		radioPanel.add(applyButton);
		
		if(themeSettings.isDefaultTheme()) {
			defaultSetting.setSelected(true);
		} else {
			darkSetting.setSelected(true);
		}
		
		ThemeListener listener = new ThemeListener(themeSettings);
		defaultSetting.addActionListener(listener);
		darkSetting.addActionListener(listener);
		
		this.add(radioPanel);
		
		// Create the font size options
		JPanel fontPanel = new JPanel();
		JLabel fontLabel = new JLabel("Font size: ");
		
		JComboBox<Integer> fontSizes = new JComboBox<>();
		for (int i = 2; i < 40; i += 2) {
//			fontSizes.add();
		}
		
		fontPanel.add(fontLabel);
		
		constraint.gridx = 0;
		constraint.gridy = 1;
		constraint.fill = GridBagConstraints.BOTH;
		constraint.weightx = 1;
		constraint.weighty = 1;
		
		this.add(fontPanel, constraint);
		
		// Create editor window
		JScrollPane mainScroll = new JScrollPane(editor);
		
		Font font = new Font(Font.MONOSPACED,
							 Font.PLAIN, 
							 themeSettings.getFontSize());

		// Create the script area
		editor.setEditable(false);
		editor.setFont(font);
		editor.setBackground(themeSettings.getEditorColour());
		
		mainScroll.setPreferredSize(new Dimension(400, 200));	
		mainScroll.setBackground(new Color(217, 217, 217));

		tln = new TextLineNumber(editor);
		tln.setBackground(themeSettings.getEditorColour());
		mainScroll.setRowHeaderView(tln);
		
		editor.setText(textString);
		editor.setCaretPosition(0);
		
		constraint.gridx = 0;
		constraint.gridy = 2;
		constraint.fill = GridBagConstraints.BOTH;
		constraint.weightx = 1;
		constraint.weighty = 1;
		
		this.getContentPane().add(mainScroll, constraint);
		
		this.validate();
		this.pack();
		this.isAlwaysOnTop();
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
			
			editor.setFont(new Font(Font.MONOSPACED,
						   			Font.PLAIN,
						   			themeSettings.getFontSize()));
			
			tln.setBackground(themeSettings.getEditorColour());
			tln.setForeground(themeSettings.getLineNumberColour());
			
			editor.setBackground(themeSettings.getEditorColour());
			editor.setText(textString);
			editor.setCaretPosition(0);
			
			repaint();
			revalidate();
		}
	}
	
}
