package com.ibm.services.tools.wexws.domain;

public class ServiceServiceArea {

	private String service;
	private String serviceArea;
	
	public ServiceServiceArea(String service, String serviceArea) {
		super();
		this.service = service;
		this.serviceArea = serviceArea;
	}
	
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getServiceArea() {
		return serviceArea;
	}
	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}
}
