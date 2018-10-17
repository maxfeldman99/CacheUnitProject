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

	private static final int PORT = 12345;
	private static boolean SERVER_IS_RUNNING = true;
	private static final int MAX_CLIENTS = 5;
	private static String serverStatus = "on";
	private Thread thread;
	private CacheUnitController<Request<String>> cacheUnitController;
	private ThreadPoolExecutor threadPoolExecutor;
	ServerSocket serverSocket;
	Socket socket;

	public Server() {
		cacheUnitController = new CacheUnitController<Request<String>>();
		threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_CLIENTS);
		// i'm using threadPool to let several clients to access the server
	}

	public void propertyChange(PropertyChangeEvent evt) { // to get updates , server is observing for changes from CLI
		serverStatus = (String) evt.getNewValue();
	}

	@Override
	public void run() {

		try { // opening new connection
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (SERVER_IS_RUNNING) {
			try {
				socket = serverSocket.accept();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {

				thread = new Thread(new HandleRequest<Request<String>>(socket, cacheUnitController));
				threadPoolExecutor.submit(thread); // number of threads was incremented
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (serverStatus.equals("off") && threadPoolExecutor.getActiveCount() == 0) {
				try {
					threadPoolExecutor.shutdown(); // turning off the socket and threadPool
				} catch (Exception e) {
					e.printStackTrace();
				} finally { // finally block to close the stream safely
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
