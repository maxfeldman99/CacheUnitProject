package com.max.server;

import java.io.Serializable;
import java.util.Map;

public class Request<T> implements Serializable {

	private Map<String, String> headers;
	private T body;

	Request(Map<String, String> headers, T body) {
		this.headers = headers;
		this.body = body;
	}

	public Map<String, String> getHeaders() {

		return this.headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public T getBody() {
		return this.body;

	}

	public void setBody(T body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Request [headers=" + headers + ", body=" + body + "]";
	}

}
