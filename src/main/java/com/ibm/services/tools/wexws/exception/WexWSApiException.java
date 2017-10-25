package com.ibm.services.tools.wexws.exception;

public class WexWSApiException extends RuntimeException {

	private static final long serialVersionUID = 134073862556420037L;

	public WexWSApiException(String message) {
        super(message);
    }
	
	public WexWSApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
