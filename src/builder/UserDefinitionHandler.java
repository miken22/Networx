package builder;

import java.util.ArrayList;

public class UserDefinitionHandler {
	
	private String script;
	private ArrayList<String> userMethods = new ArrayList<>();
	
	/**
	 * Finds all user defined classes and methods and extracts them from the
	 * script
	 * 
	 * @param theScript
	 * @return
	 */
	public ArrayList<String> findUserDefinitions(String theScript) {
		
		this.script = theScript;		
		
		int position = script.indexOf("public");

		if (position != -1) {
			this.script = extractUserMethod(script, position, userMethods);		// Global copy of trimmed String
			findUserDefinitions(script);	// Continue to search for more methods
		}

		position = script.indexOf("private");
		if (position != -1) {
			this.script = extractUserMethod(script, position, userMethods);
			findUserDefinitions(script);
		}

		position = script.indexOf("protected");
		if (position != -1) {
			this.script = extractUserMethod(script, position, userMethods);
			findUserDefinitions(script);
		}

		return userMethods;
	}

	private String extractUserMethod(String theScript, int position, ArrayList<String> userMethods) {

		// Store starting index of method.
		int startLocation = position;
		int braceCounter = 0;
		// Find first occurrence of open brace
		position = theScript.indexOf("{", position);

		// Update the counter, shift to next index in string
		braceCounter++;
		position++;

		while (braceCounter > 0) {

			for (int i = position; i < theScript.length(); i++) {
				char c = theScript.charAt(i);

				if (c == '{') {
					braceCounter++;
				} else if (c == '}') {
					braceCounter--;
				}					

				if (braceCounter == 0) {
					position = i;
					break;
				}
			}
		}

		position++;	// Shift to next index in String (if it exists)

		userMethods.add(theScript.substring(startLocation, position));

		String remainingCode = "";
		// Store remaining code (if any)
		try {
			remainingCode = theScript.substring(position);
		} catch (IndexOutOfBoundsException e) {
			// Means we are ok, reached the end of the script
			theScript = theScript.substring(0, startLocation);
			theScript += "\r\n" + remainingCode;
			return theScript;
		}

		theScript = theScript.substring(0, startLocation);
		theScript += "\r\n" + remainingCode;
		// Otherwise more code to examine.
		return theScript;
	}
	
	public void clearOldMethods() {
		userMethods.clear();
	}
	
	public String getCleanedScript() {
		return script;
	}

}
