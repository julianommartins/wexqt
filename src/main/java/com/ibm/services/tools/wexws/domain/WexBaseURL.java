package com.ibm.services.tools.wexws.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.ibm.services.tools.wexws.WexConfiguration;

public class WexBaseURL {

	private static Logger logger = Logger.getLogger(WexBaseURL.class);
	
	protected String server;
	protected WexConfiguration configuration;

	public WexBaseURL(String server, WexConfiguration configuration) {
		super();
		this.server = server;
		this.configuration = configuration;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	protected String encodeValue(String value) {
		String encodedValue = "";
		if (value != null){
			try {
				encodedValue = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("UnsupportedEncodingException in encodeValue(), msg="+e.getMessage());
			}
		}
		return encodedValue;
	}

	protected void appendBasicServerParameters(StringBuilder sb) {
		sb.append("http://").append(server).append(":").append(configuration.getPort()).append("/vivisimo/cgi-bin/velocity?v.app=api-rest");
		sb.append("&v.username=").append(configuration.getUser()).append("&v.password=").append(encodeValue(configuration.getPassword()));
	}

}