package com.ibm.services.tools.wexws.utils;

import java.util.concurrent.Callable;

public class HttpRequestCallable implements Callable<String> {

	private final HttpRequest httpRequest;
		
	public HttpRequestCallable(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	@Override
	public String call() throws Exception {
		String resp = httpRequest.doGet();
		return resp;
	}

}
