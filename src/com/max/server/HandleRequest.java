package com.max.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.max.dm.DataModel;
import com.max.services.CacheUnitController;
import java.lang.reflect.Type;

public class HandleRequest<T> implements Runnable {

	private Socket socket;
	private CacheUnitController<T> controller;
	private static final int PORT = 12345;
	private static final String GET = "GET";
	private static final String DEL = "DELETE";
	private static final String UPDATE = "UPDATE";
	private static final String ACTION = "action";

	HandleRequest(Socket s, CacheUnitController<T> controller) {
		this.socket = s;
		this.controller = controller;
	}

	@Override
	public void run() {
		try {
//			InetAddress address = InetAddress.getLocalHost();
//			Socket socket = new Socket(address, PORT);

			new HandleRequest<String>(socket, new CacheUnitController<String>());

			ObjectInputStream inputStream = null;
			ObjectOutputStream OutputStream = null;

			OutputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());

			String req = (String) inputStream.readObject();
			Type ref = new TypeToken<Request<DataModel<T>[]>>() {
			}.getType();
			Request<DataModel<T>[]> request = new Gson().fromJson(req, ref);

			Map<String, String> map = request.getHeaders();
			DataModel<T>[] requestModels = request.getBody();
			DataModel<T>[] resultModels = null;
			boolean requestResult = false;

			String requestAction = map.get(ACTION);

			switch (requestAction) {
			case GET:
				resultModels = controller.get(requestModels);
				if (resultModels != null) { // if there is nothing to return the result will be false
					requestResult = true;

				}
				break;
			case DEL:
				requestResult = controller.delete(requestModels);
				break;
			case UPDATE:
				requestResult = controller.update(requestModels);
				break;
			}

			Gson gson = new Gson(); // this section will convert our data to JSON and send it
			String toSend;
			if (requestModels != null && requestAction.equals(ACTION)) {
				toSend = gson.toJson(resultModels);
			} else {
				toSend = gson.toJson(requestResult);
			}
			OutputStream.writeObject(toSend);
			System.out.println("message from server: " + toSend);  // just for test

			OutputStream.flush();
			inputStream.close();
			OutputStream.close();
			socket.close();

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
