package Launcher;

import IDE.LoadingScreen;
import IDE.MainPane;

public class Launch {

	public static void main(String[] args){
		
		/* Does not work...
			LoadingScreen splash = new LoadingScreen();
			splash.launch();
		*/
		
		MainPane pane = new MainPane();
		pane.loadWorkbench();
	}
}
