package ide;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;

public class TextEditorDocument extends DefaultStyledDocument {

	private static final long serialVersionUID = 1L;
    private SimpleAttributeSet reservedWords;
    private SimpleAttributeSet keyWords;
    private SimpleAttributeSet defaultColour;
    private SimpleAttributeSet quotations;
    private SimpleAttributeSet comments;
    
    public TextEditorDocument() {
    	
    	reservedWords = new SimpleAttributeSet();
    	keyWords = new SimpleAttributeSet();
    	defaultColour = new SimpleAttributeSet();
    	quotations = new SimpleAttributeSet();
    	comments = new SimpleAttributeSet();
    	
    	StyleConstants.setForeground(reservedWords, Color.RED);
    	StyleConstants.setBold(reservedWords, true);
    	
    	StyleConstants.setForeground(keyWords, new Color(255,69,0));
    	StyleConstants.setBold(keyWords, true);
    	
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
                if (text.substring(wordL, wordR).matches("(\\W)*(" + ReservedWords.reservedWords + ")")) {
                    setCharacterAttributes(wordL, wordR - wordL, reservedWords, false);
                } else if (text.substring(wordL, wordR).matches("(\\W)*(" + KeyWords.keyWords + ")")) {
                    setCharacterAttributes(wordL, wordR - wordL, keyWords, false);                	
                           	            	
            	}else {
                
                    setCharacterAttributes(wordL, wordR - wordL, defaultColour, false);
                }                
                wordL = wordR;
            }
            wordR++;
        }       
        
        colourComments(text);

    }
	
	//TODO: Fix colouring of quoted text, add comment colouring

	private void colourComments(String text) {
		
		StringReader strReader = new StringReader(text);
		BufferedReader reader = new BufferedReader(strReader);
		
		String line = null;
		try {
			int position = 0;
			line = reader.readLine();
			while (line != null) {
				
				if (line.startsWith("//")) {
					int length = line.length();
		            setCharacterAttributes(position, length, comments, false);     
				}
				position += line.length()+1;
				line = reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

//		if ((text.substring(wordL, wordR).matches("(\\/)"))) { 
//            setCharacterAttributes(wordL, wordR - wordL, comments, false);     
//		}
		
	}

	public void remove(int offset, int length) throws BadLocationException {
        super.remove(offset, length);

        String text = getText(0, getLength());
        int before = findLastNonWordChar(text, offset);
        if (before < 0) before = 0;
        int after = findFirstNonWordChar(text, offset);

        if (text.substring(before, after).matches("(\\W)*(" + ReservedWords.reservedWords + ")")) {
            setCharacterAttributes(before, after - before, reservedWords, false);
        } else if (text.substring(before, after).matches("(\\W)*(" + KeyWords.keyWords + ")")) {
                setCharacterAttributes(before, after - before, keyWords, false);
        } else {
            setCharacterAttributes(before, after - before, defaultColour, false);
        }
        
        colourComments(text);

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
