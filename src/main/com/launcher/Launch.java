package main.com.launcher;

import java.awt.EventQueue;

import javax.swing.JOptionPane;

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
				
				String OS = System.getProperty("os.name");
				
				if (OS.indexOf("mac") >= 0) {
					JOptionPane.showMessageDialog(null, "Sorry, only Windows and Linux currently supported.");
					System.exit(-1);			
				}
				
			    WorkBench pane = new WorkBench();
			    pane.loadWorkbench();
			}
		}; 

		EventQueue.invokeLater(task);
	
	}
}
