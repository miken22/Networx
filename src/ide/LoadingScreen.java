package ide;

import java.awt.SplashScreen;
import java.io.IOException;
import java.net.URL;

public class LoadingScreen {

	public void launch() {

		SplashScreen screen = SplashScreen.getSplashScreen();
//		System.out.println(this.getClass().getClassLoader().getResource("resources/splash.gif"));
		
		try {
			URL splashPath = this.getClass().getClassLoader().getResource("resources/splash.gif");
			screen.setImageURL(splashPath);
		} catch (NullPointerException | IllegalStateException | IOException e) {
			e.printStackTrace();
		}
	    for (int i = 0; i < 100; i++) {
	    	screen.update();
	    }
	}

}
