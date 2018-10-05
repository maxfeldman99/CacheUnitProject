package com.hit.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.lang.reflect.Type;

public class HandleRequest<T> implements Runnable {

	private Socket socket;
	private CacheUnitController<T> controller;  // <T> after cuc?

	
	private static final int PORT = 12345;
	private static final String GET = "GET";
	private static final String DEL = "DELETE";
	private static final String UPDATE = "UPDATE";
	private static final String ACTION = "action";


	public HandleRequest(Socket s, CacheUnitController<T> controller) {
		this.socket = s;
		this.controller = controller;
	}

	@Override
	public void run() {
		OutputStreamWriter outputStream = null;
		String req = null;
		
		try {
//			InetAddress address = InetAddress.getLocalHost();
//			Socket socket = new Socket(address, PORT);	
			
		//	new HandleRequest<String>(socket, new CacheUnitController<String>());
			
			Scanner reader = new Scanner(new InputStreamReader(socket.getInputStream()));
			
			//Scanner scanner = new Scanner(new InputStreamReader(socket.getInputStream()));
			outputStream =new OutputStreamWriter(socket.getOutputStream());
			//inputStream = new InputStreamReader(socket.getInputStream());
			
			while(reader.hasNextLine()){
				 req = reader.nextLine();
			}
		
			
			System.out.println(req);
	
			Type ref = new TypeToken<Request<DataModel<T>[]>>() {
			}.getType();
			Request<DataModel<T>[]> request = new Gson().fromJson(req, ref);
			

			
			Map<String, String> map = request.getHeaders();
			DataModel<T>[] requestModels = request.getBody();
			DataModel<T>[] resultModels = null;
			boolean requestResult = false;

			String requestAction = map.get(ACTION);
			
			System.out.println("number of models inside request is : "+requestModels.length);
			for (int i = 0; i < requestModels.length; i++) {
				System.out.println(requestModels[i].toString());
				
			}
			
			System.out.println(requestAction);
			
			
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
			
			System.out.println("message from serveRRR: " + toSend); // just for test
			
			outputStream.write(toSend);
			
			outputStream.flush();
	
	

		} catch (Exception e ) {
			e.printStackTrace();
			
		}
		
//		finally { // finally as requested
//			
//			try {
//				if (inputStream != null)
//					inputStream.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			try {
//				if (outputStream != null) {
//					outputStream.flush();
//					
//				}
//				outputStream.close();
//					
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}

	}
	
}
