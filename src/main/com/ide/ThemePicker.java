package main.com.ide;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
public class ThemePicker extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	/**
	 * The theme settings for the main text editor environment
	 */
	private ThemeSettings settings;

	/*
	 * The components that make up the example editor to preview the
	 * different theme. There are global because the action listener
	 * for the radio buttons needs to be able to modify these settings
	 * and it helps limit the amount of parameters that would need
	 * to be passed. 
	 */
	private TextEditorDocument textarea = new TextEditorDocument();
	private TextEditor editor = new TextEditor(textarea);
	private TextLineNumber tln;

	/**
	 * This is passed from the workbench as the action listener that initiates
	 * the repaint of the main frame.
	 */
	private ActionListener applyListener;

	private Integer fontSize = null;

	private final String textString = "// This is a comment \n\r" +
			"public void testMethod() { \r\n" +
			"    int i = 0;\r\n" +
			"    System.out.println(\"i equals:\" + i);\r\n" +
			"    return;\r\n" +
			"}\r\n";

	public ThemePicker(ThemeSettings themeSettings, ActionListener applyListener) {
		this.settings = themeSettings;		
		this.applyListener = applyListener;
		createFrame();
	}

	private void createFrame() {

		this.setLayout(new GridBagLayout());
		this.setBounds(0, 0, 450, 400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Create the font size options
		JPanel fontPanel = new JPanel();
		JLabel fontLabel = new JLabel("Font size: ");

		// Load list of sizes
		Integer[] sizes = new Integer[20];
		for (int i = 2; i < 41; i += 2) {
			int index = (i-2)/2;
			sizes[index] = i;
		}

		JComboBox<Integer> fontSizes = new JComboBox<>(sizes);
		fontSizes.setEditable(false);
		fontSizes.addActionListener(this);
		fontSizes.setSelectedItem(settings.getFontSize());
		
		fontPanel.add(fontLabel);
		fontPanel.add(fontSizes);
		this.add(fontPanel);

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
		JButton cancelButton = new JButton("Cancel");

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

		// Do nothing, get rid of frame
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setBackground(new Color(239, 54, 54).brighter());
		
		// Create the panel for the radio buttons and place each
		// component
		radioPanel.setLayout(new GridLayout(1,4));
		radioPanel.add(defaultSetting);
		radioPanel.add(darkSetting);
		radioPanel.add(applyButton);
		radioPanel.add(cancelButton);

		if(settings.isDefaultTheme()) {
			defaultSetting.setSelected(true);
		} else {
			darkSetting.setSelected(true);
		}

		ThemeListener listener = new ThemeListener();
		defaultSetting.addActionListener(listener);
		darkSetting.addActionListener(listener);

		this.add(radioPanel);

		// Create editor window
		JScrollPane mainScroll = new JScrollPane(editor);

		Font font = new Font(Font.MONOSPACED,
				Font.PLAIN, 
				settings.getFontSize());

		// Create the script area
		editor.setEditable(false);
		editor.setFont(font);
		editor.setBackground(settings.getEditorColour());

		mainScroll.setPreferredSize(new Dimension(400, 200));	
		mainScroll.setBackground(new Color(217, 217, 217));

		tln = new TextLineNumber(editor);
		tln.setBackground(settings.getEditorColour());
		mainScroll.setRowHeaderView(tln);

		editor.setText(textString);
		editor.setCaretPosition(0);

		GridBagConstraints constraint = new GridBagConstraints();

		constraint.gridx = 0;
		constraint.gridy = 2;
		constraint.fill = GridBagConstraints.BOTH;
		constraint.weightx = 1;
		constraint.weighty = 1;
		constraint.gridwidth = 2; // span both columns

		this.add(mainScroll, constraint);
		
		this.validate();
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	
	}

	/**
	 * This method is used at the action listener for the combobox on the frame.
	 * It should not be used as a listener for ANYTHING else.
	 * 
	 * @param e The event triggering the listener.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// We know this will be safe since it will never be used
		// as a listener to any other class, and this class uses
		// an uneditible combobox of integers.  
		
		// Cast to generic
		JComboBox<?> cb = (JComboBox<?>) e.getSource();
		fontSize = (Integer)cb.getSelectedItem();
		settings.setFontSize(fontSize);

		Font font = new Font(Font.MONOSPACED,
				 			 Font.PLAIN,
				 			 fontSize);
		
		// Update editor font
		editor.setFont(font);
		
		// Update the frame
		repaint();
		revalidate();
	}

	/**
	 * Nested inner class that updates the theme of the sample
	 * text editor whenever the radio button is pressed
	 * 
	 * @author Michael Nowicki
	 *
	 */
	private class ThemeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getActionCommand().equals("default")) {
				settings.setDefaultTheme();
			} else {
				settings.setDarkTheme();				
			}

			textarea.setQuotations(settings.getQuotations());
			textarea.setReservedWords(settings.getReservedWords());
			textarea.setComments(settings.getComments());
			textarea.setDefaultColour(settings.getDefaultColour());

			Font font = new Font(Font.MONOSPACED,
								 Font.PLAIN,
								 settings.getFontSize());

			
			tln.setBackground(settings.getEditorColour());
			tln.setForeground(settings.getLineNumberColour());
			tln.setFont(font);

			editor.setFont(font);
			editor.setBackground(settings.getEditorColour());
			editor.setText(textString);
			editor.setCaretPosition(0);

			repaint();
			revalidate();
		}
	}

}
