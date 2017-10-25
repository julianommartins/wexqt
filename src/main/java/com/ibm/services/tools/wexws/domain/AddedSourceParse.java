package com.ibm.services.tools.wexws.domain;

import javax.xml.bind.annotation.XmlAttribute;

public class AddedSourceParse {
	
	private static final String[] errorStatuses = new String[]{"parsing-failed", "failed", "connection-failed", "dns-timeout", 
			"timeout", "url-encoding-error", "content-encoding-error", "http-error", "too-deep", "malformed-url", "base64-decoding-failed"};
	
	private String status;

	public String getStatus() {
		return status;
	}
	@XmlAttribute(name="status")
	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean hasErrors(){
		boolean hasErrors = false;
		if (status != null){
			for (String errorStatus: errorStatuses){
				if (status.contains(errorStatus)){
					hasErrors = true;
					break;
				}
			}
		}
		return hasErrors;
	}
}
