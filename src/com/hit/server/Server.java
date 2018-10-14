package com.hit.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.hit.services.CacheUnitController;

//Our observer

public class Server implements PropertyChangeListener, Runnable {

	// private static final int TIME_OUT_TIME = 10000;
	private static final int PORT = 12345;
	private static boolean REQUEST_IS_RUNNING = true;
	private static boolean SERVER_IS_RUNNING = true;
	private static final int MAX_CLIENTS = 5;
	private static String serverStatus = "on";
	// private ObjectInputStream objectInputStream = null;
//	private ObjectOutputStream objectOutputStream = null;
	private Thread thread;
	private CacheUnitController<Request<String>> cacheUnitController;
	private ThreadPoolExecutor threadPoolExecutor;
	ServerSocket serverSocket;
	Socket socket;

	public Server() {
		cacheUnitController = new CacheUnitController<Request<String>>(); // maybe not needed in this position
		threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_CLIENTS);

	}

	public void propertyChange(PropertyChangeEvent evt) {
		serverStatus = (String) evt.getNewValue(); 
	}

	@Override
	public void run() {

		try {
			serverSocket = new ServerSocket(PORT);
			//socket = serverSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (SERVER_IS_RUNNING) {
			try {
				socket = serverSocket.accept();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				try {
					
					System.out.println(" number of activated threads " + threadPoolExecutor.getActiveCount());
					// serverSocket.setSoTimeout(TIME_OUT_TIME);
					// ObjectInputStream objectInputStream = new
					// ObjectInputStream(socket.getInputStream());
					// ObjectOutputStream objectOutputStream = new
					// ObjectOutputStream(socket.getOutputStream());
//				objectOutputStream.writeObject(" You are Connected! ");
//				objectOutputStream.flush();

					// should the inputMsg should be "message" ?
					// String inputMsg = (String) objectInputStream.readObject();

					thread = new Thread(new HandleRequest<Request<String>>(socket, cacheUnitController)); // not sure
																											// about the
																											// <>
																											// arguments
					
					threadPoolExecutor.submit(thread);
					System.out.println(" number of activated threads " + threadPoolExecutor.getActiveCount());
					

					// System.out.println("message from the client: " + inputMsg);
//				objectOutputStream.writeObject("closing conneciton");
//				objectOutputStream.flush();

					// System.out.println(" number of activated threads " +
					// threadPoolExecutor.getActiveCount());

				} catch (Exception e) {
					e.printStackTrace();

				}

				System.out.println("exiting server loop");
				REQUEST_IS_RUNNING = false;


			if (serverStatus.equals("off") && threadPoolExecutor.getActiveCount() == 0) {
				try {
					System.out.println("turning off the socket");
					threadPoolExecutor.shutdown();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						socket.close();
						serverSocket.close();
						SERVER_IS_RUNNING = false;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

		}
	}
}
