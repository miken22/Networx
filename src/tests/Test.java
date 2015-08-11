package tests;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Test {

	public static void main(String[] args) throws IOException, URISyntaxException {
		
		String path = Test.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		
		if(Desktop.isDesktopSupported())
		{
		  Desktop.getDesktop().browse(new URI("file://" + path + "doc/index.html"));
		}

	}

}
