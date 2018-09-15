package com.hit.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//Our observer

public class Server implements PropertyChangeListener, Runnable {

	private static final int TIME_OUT_TIME = 10000;
	private static final int PORT = 12345;
	private String message;
	ObjectInputStream objectInputStream = null;
	ObjectOutputStream objectOutputStream  = null;

	public Server() {

	}

	public void propertyChange(PropertyChangeEvent evt) {
		this.message = (String) evt.getNewValue(); // not sure
	}

	@Override
	public void run() {

		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			Socket socket = serverSocket.accept();
			serverSocket.setSoTimeout(TIME_OUT_TIME);
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject("something");
			objectOutputStream.flush();
			String inputMsg = (String) objectInputStream.readObject();
			System.out.println("message from the client: " + inputMsg);
			objectOutputStream.writeObject("closing conneciton");
			objectOutputStream.flush();
			socket.close();
			serverSocket.close();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if (objectOutputStream != null)
					objectOutputStream.flush();
				objectOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


}
