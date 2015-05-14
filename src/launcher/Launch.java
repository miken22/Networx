package launcher;

import ide.MainPane;

public class Launch {

	public static void main(String[] args){
		
		//Does not work...
		//LoadingScreen splash = new LoadingScreen();
		//splash.launch();
		
		MainPane pane = new MainPane();
		pane.loadWorkbench();
	}
}
