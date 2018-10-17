package com.hit.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;
import com.hit.util.RequestStatistics;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Map;

public class HandleRequest<T> implements Runnable {

	private CacheUnitController<T> controller;

	private Socket socket;
	private static final String GET = "GET";
	private static final String DEL = "DELETE";
	private static final String UPDATE = "UPDATE";
	private static final String ACTION = "action";
	private static final String STATS = "statistics";
	private StringBuilder builder = new StringBuilder("");

	public HandleRequest(Socket socket, CacheUnitController<T> controller) {

		this.controller = controller;
		this.socket = socket;

	}

	@Override
	public void run() {
		Gson gson = new GsonBuilder().create();
		ObjectInputStream inputStream = null;
		ObjectOutputStream outputStream = null;
		String req = null;
		String stats = null;
		String answer = null;
		boolean requestResult = false;

		try {

			inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			req = (String) inputStream.readObject();

			Type ref = new TypeToken<Request<DataModel<T>[]>>() {
			}.getType();
			Request<DataModel<T>[]> request = gson.fromJson(req, ref); // conversion of the JSON data
			Map<String, String> map = request.getHeaders();
			DataModel<T>[] requestModels = request.getBody();
			DataModel<T>[] resultModels = null;
			RequestStatistics.getInstance().incrementReqNum(requestModels.length);
			
			String requestAction = map.get(ACTION);
			
			switch (requestAction) { // this section will decide which action to use
			case GET:
				resultModels = controller.get(requestModels);
				if (resultModels != null) { // if there is nothing to return the result will be false
					requestResult = true;
					break;
				}
				// else no such elements
				break;

			case DEL:
				requestResult = controller.delete(requestModels);
				// requestResult could be true or false if deleted
				break;

			case UPDATE:
				requestResult = controller.update(requestModels);  // updating they current value
				break;
				// requestResult could be true or false if updated
			}

			if (requestAction.equals(STATS)) {
				outputStream.writeObject(stats);
			} else {
				String toSendBack = controller.getUnitStatistics();
				answer = String.valueOf(requestResult); // optional
				outputStream.writeObject(toSendBack);
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}		finally { // finally block 
			try {
				if (inputStream != null) 
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void write(String string) {
		System.out.println(builder.append(string));
		builder.delete(0, string.length());
	}

}
