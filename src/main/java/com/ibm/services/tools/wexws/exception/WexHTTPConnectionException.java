package com.ibm.services.tools.wexws.exception;

public class WexHTTPConnectionException extends RuntimeException {

	private static final long serialVersionUID = 1;
	
	public WexHTTPConnectionException(String message){
		super(message);
	}
	public WexHTTPConnectionException(String message, Throwable throwable){
		super(message, throwable);
	}
}
