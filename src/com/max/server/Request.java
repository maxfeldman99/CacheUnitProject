package com.max.server;

import java.io.Serializable;
import java.util.Map;

public class Request<T> implements Serializable {
	
	private Map<String,String> headers;
	private T body;

	Request(Map<String,String> headers, T body){
		this.headers = headers;
		this.body = body;
	}
		public Map<String,String> getHeaders(){
			return null;
		}
		
		public void setHeaders(Map<String,String> headers) {
			
		}
		
		public T getBody() {
			return null;
			
		}
		
		public void setBody(T body) {
			
		}
		
		@Override
		public String toString() {
			return super.toString();
		}
		
		
	
}
