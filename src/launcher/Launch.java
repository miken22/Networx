package launcher;

import java.awt.EventQueue;

import ide.WorkBench;

public class Launch {

	public static void main(String[] args){	
		EventQueue.invokeLater(new Runnable() {
	        public void run() {
	    		WorkBench pane = new WorkBench();
	    		pane.loadWorkbench();
	        }
	    });
	}
}
