package com.hit.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.hit.services.CacheUnitController;

//Our observer

public class Server implements PropertyChangeListener, Runnable {

	//private static final int TIME_OUT_TIME = 10000;
	private static final int PORT = 12345;
	private static boolean SERVER_IS_ON = true;
	private static final int MAX_CLIENTS = 10;
	private String message;
	private ObjectInputStream objectInputStream = null;
	private ObjectOutputStream objectOutputStream = null;
	private Thread thread;
	private  CacheUnitController<Request<String>> cacheUnitController;
	private ThreadPoolExecutor threadPoolExecutor;
	ServerSocket serverSocket;
	Socket socket;

	public Server() {
		cacheUnitController = new CacheUnitController<Request<String>> (); // maybe not needed in this position
		threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool (MAX_CLIENTS);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		this.message = (String) evt.getNewValue(); // not sure
	}

	@Override
	public void run() {

		while (SERVER_IS_ON) {
			try {
				serverSocket = new ServerSocket(PORT);
				socket = serverSocket.accept();
				//serverSocket.setSoTimeout(TIME_OUT_TIME);
				ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
				objectOutputStream.writeObject(" You are Connected! ");
				objectOutputStream.flush();
				

				// should the inputMsg should be "message" ?

				String inputMsg = (String) objectInputStream.readObject();
				
				
				thread = new Thread(new HandleRequest<Request<String>>(socket,cacheUnitController)); // not sure about the <> arguments
				thread.start();
				
				System.out.println(" number of activated threads " + threadPoolExecutor.getActiveCount());
				
				System.out.println("message from the client: " + inputMsg);
				objectOutputStream.writeObject("closing conneciton");
				objectOutputStream.flush();
				
				threadPoolExecutor.submit(thread);
				
				System.out.println(" number of activated threads " + threadPoolExecutor.getActiveCount());

			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();

			} finally {
				try {
					if (objectOutputStream != null) {
						objectOutputStream.flush();
						objectOutputStream.close(); // not sure if supposed to be inside the if statement, generates an error sometimes
						socket.close();
					    serverSocket.close();
					}
					
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			threadPoolExecutor.shutdown();
			SERVER_IS_ON = false;
		}

	}

}
