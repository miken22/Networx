package ide;

import java.awt.SplashScreen;
import java.io.IOException;

public class LoadingScreen {

	public void launch() {

		SplashScreen screen = SplashScreen.getSplashScreen();
	
		try {
			screen.setImageURL(getClass().getResource("/resources/splash.gif"));
		} catch (NullPointerException | IllegalStateException | IOException e) {
			e.printStackTrace();
		}
	    for (int i = 0; i < 100; i++) {
	    	screen.update();
	    }
	}

}
