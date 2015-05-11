package ide;

import java.awt.Color;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;

public class TextEditorDocument extends DefaultStyledDocument {

	private static final long serialVersionUID = 1L;
    private SimpleAttributeSet reservedWords;
    private SimpleAttributeSet defaultColour;
    private SimpleAttributeSet quotations;
    private SimpleAttributeSet comments;
    
    public TextEditorDocument() {
    	
    	reservedWords = new SimpleAttributeSet();
    	defaultColour = new SimpleAttributeSet();
    	quotations = new SimpleAttributeSet();
    	comments = new SimpleAttributeSet();
    	
    	StyleConstants.setForeground(reservedWords, Color.RED);
    	StyleConstants.setBold(reservedWords, true);
    	
    	StyleConstants.setForeground(defaultColour, Color.BLACK);
    	StyleConstants.setBold(defaultColour, false);
    	
    	StyleConstants.setForeground(quotations, Color.BLUE);
    	StyleConstants.setBold(quotations, false);
    	StyleConstants.setItalic(quotations, false);
    	
    	StyleConstants.setForeground(comments, Color.GREEN);
    	StyleConstants.setBold(quotations, false);
    	StyleConstants.setItalic(quotations, true);  	
    	
    }
    
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offset, str, a);

        String text = getText(0, getLength());
        int before = findLastNonWordChar(text, offset);
        if (before < 0) before = 0;
        int after = findFirstNonWordChar(text, offset + str.length());
        int wordL = before;
        int wordR = before;

        while (wordR <= after) {
            if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                if (text.substring(wordL, wordR).matches("(\\W)*(private|public|protected|final|super|if|while|do|void|true|null|false|else|System|static)")) {
                    setCharacterAttributes(wordL, wordR - wordL, reservedWords, false);
                } else {
                    setCharacterAttributes(wordL, wordR - wordL, defaultColour, false);
                }
                wordL = wordR;
            }
            wordR++;
        }
    }
	
	//TODO: Fix colouring of quoted text, add comment colouring
	
    public void remove(int offset, int length) throws BadLocationException {
        super.remove(offset, length);

        String text = getText(0, getLength());
        int before = findLastNonWordChar(text, offset);
        if (before < 0) before = 0;
        int after = findFirstNonWordChar(text, offset);

        if (text.substring(before, after).matches("(\\W)*(private|public|protected|final|super|if|while|do|void|true|null|false|else|System|static)")) {
            setCharacterAttributes(before, after - before, reservedWords, false);
        } else {
            setCharacterAttributes(before, after - before, defaultColour, false);
        }
        
    }
	
    private int findLastNonWordChar(String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }
}
