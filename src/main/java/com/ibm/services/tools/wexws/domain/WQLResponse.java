package com.ibm.services.tools.wexws.domain;

public class WQLResponse {
	
	private String wql;
	private String response;
	private String error;
	
	public String getWql() {
		return wql;
	}
	public void setWql(String wql) {
		this.wql = wql;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

}
