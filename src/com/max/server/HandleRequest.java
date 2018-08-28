package com.max.server;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;

import com.max.services.CacheUnitController;

public class HandleRequest<T> implements Runnable {

	private static final int PORT = 12345;

	HandleRequest(Socket s, CacheUnitController<T> controller) {

	}

	@Override
	public void run() {
		try {
			
			Socket socket = new Socket("localhost", PORT);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			String msgFromServer = (String) objectInputStream.readObject(); // need to be replaced with request
			Request<T> request = (Request<T>)objectInputStream.readObject(); // not sure
			
			
			System.out.println("message from server: " + msgFromServer);
			objectOutputStream.writeObject("someThing");
			objectOutputStream.flush();
			objectInputStream.close();
			objectOutputStream.close();
			socket.close();
			
			Gson gson = new Gson();
			request = gson.fromJson(new FileReader("Request.json"), Request.class); // not sure what file reader should receive
					

			
			
			
			
			

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
