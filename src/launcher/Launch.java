package launcher;

import java.awt.EventQueue;

import ide.WorkBench;

public class Launch {

	public static void main(String[] args){	
	
		Runnable task = new Runnable(){
			@Override
			public void run() {
			    WorkBench pane = new WorkBench();
			    pane.loadWorkbench();
			}
		}; 

		EventQueue.invokeLater(task);
	
	}
}
