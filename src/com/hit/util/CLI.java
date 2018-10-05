package com.hit.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import com.hit.server.Server;

//our observable

public class CLI implements Runnable {

	private PropertyChangeSupport support = new PropertyChangeSupport(this);
	private Scanner scanner;
	private PrintWriter printWriter;
	private StringBuilder builder = new StringBuilder("");
	private static final String STARTING = "starting server....";
	private static final String SHUTDOWN = "shutdown server";
	private static final String INVALID = "unknown command";
	private static final String ENTER_COMMAND = "Please Enter Your Command: ";
	private Thread thread;
	private static String SERVER= "on";
	
	public CLI(InputStream in, OutputStream out) {

		scanner = new Scanner(in);
		printWriter = new PrintWriter(out);
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		support.removePropertyChangeListener(pcl);
	}

	// Using this support, we can add and remove observers, and notify them when the
	// state of the observable changes

	public void write(String string) {
		System.out.println(builder.append(string));
		builder.delete(0, string.length());
	}

	@Override
	public void run() {
		while (true) {
			write(ENTER_COMMAND);
			// printWriter.println(ENTER_COMMAND);
			String input = scanner.nextLine();
			while (!input.equalsIgnoreCase("stop")) {
				// printWriter.println (ENTER_COMMAND);
				// input = scanner.nextLine ();
				if (input.equals("start")) {
					write(STARTING);
					thread = new Thread(new Server());
					thread.start();

				} else if (!input.equals("stop")) {
					write(INVALID);
					
				}
				input = scanner.nextLine();
			}
			write(SHUTDOWN);
			support.firePropertyChange(SERVER,SERVER,"off");
//			scanner.close();
		
		}
	
	}
}
