package com.max.server;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import com.max.dm.DataModel;
import com.max.services.CacheUnitController;
import java.lang.reflect.Type;


public class HandleRequest<T> implements Runnable {
	
	private Socket socket;
	private CacheUnitController<T> controller;
	private static final int PORT = 12345;

	HandleRequest(Socket s, CacheUnitController<T> controller) {
		this.socket = s;
		this.controller = controller;
	}

	@Override
	public void run() {
		try {
			
			Socket socket = new Socket("localhost", PORT);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			String msgFromServer = (String) objectInputStream.readObject(); // need to be replaced with request
			Request<T> req = (Request<T>)objectInputStream.readObject(); // not sure
			new HandleRequest<String>(socket, new CacheUnitController<String>());
			Gson gson = new Gson();
			
			System.out.println("message from server: " + msgFromServer);
			objectOutputStream.writeObject("someThing");
			objectOutputStream.flush();
			objectInputStream.close();
			objectOutputStream.close();
			socket.close();
			
			req = gson.fromJson(new FileReader("Request.json"), Request.class); // not sure what file reader should receive
			Type ref = new TypeToken<Request<DataModel<T>[]>>(){}.getType(); // must
			Request<DataModel<T>[]> request = new Gson().fromJson(req, ref); // must
			
			
			

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
