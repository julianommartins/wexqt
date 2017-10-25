package com.ibm.services.tools.wexws.exception;

public class TooManySearchKeywordsException extends WexWSApiException {
	
	private static final long serialVersionUID = 1L;

	public TooManySearchKeywordsException(int totalKeywords) {
		super("Too many keywords ("+ totalKeywords +") were used for this search. Please reduce the number of keywords to a maximum of 6 and try again.");
	}
}
