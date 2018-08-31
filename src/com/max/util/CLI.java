package com.max.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class CLI extends Object implements Runnable {

	private Scanner scanner;
	private PrintWriter printWriter;
	private static final String STARTING = "starting server....";
	private static final String SHUTDOWN = "shutdown server";
	private static final String INVALID = "unknown command";
	private static final String ENTER_COMMAND = "Please Enter Your Command: ";

	public CLI(InputStream in, OutputStream out) {
		scanner = new Scanner(in);
		printWriter = new PrintWriter(out);
	}

	void addPropertyChangeListener(PropertyChangeListener pcl) {

	}

	void removePropertyChangeListener(PropertyChangeListener pcl) {

	}

	void write(String string) {
		System.out.println(string);
	}

	@Override
	public void run() {

		write(ENTER_COMMAND);
		// printWriter.println(ENTER_COMMAND);
		String input = scanner.nextLine();
		while (!input.equalsIgnoreCase("stop")) {
			// printWriter.println (ENTER_COMMAND);
			// input = scanner.nextLine ();
			if (input.equals("start")) {
				write(STARTING);
			} else if (!input.equals("stop")) {
				write(INVALID);
			}
			input = scanner.nextLine();
		}
		write(SHUTDOWN);
		scanner.close();

	}
}
