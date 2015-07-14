package main.com.launcher;

import java.awt.EventQueue;

import main.com.ide.WorkBench;

/**
 * Main entry point of the program
 * 
 * @author Mike Nowicki
 *
 */
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
