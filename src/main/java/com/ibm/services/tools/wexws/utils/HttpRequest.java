package com.ibm.services.tools.wexws.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.ibm.services.tools.wexws.WexConfiguration;
import com.ibm.services.tools.wexws.exception.WexHTTPConnectionException;

public class HttpRequest {
	
	private static final Logger logger = Logger.getLogger(HttpRequest.class);
	
	private final String url;
	private final boolean debug;
	private final WexConfiguration configuration;
		
	public HttpRequest(String url, WexConfiguration configuration, boolean debug){
		this.url = url;
		this.configuration = configuration;
		this.debug = debug;
	}
	
	public String doGet() {

		long t = System.currentTimeMillis();
		
		String response = null;
		HttpURLConnection urlConn = null;
		try {
			int indexOf = url.indexOf("?");
			String urlHost = url.substring(0, indexOf);
			String urlParameters = url.substring(indexOf+1);
			byte[] postData = urlParameters.getBytes("UTF-8");
						
			URL mUrl = new URL(urlHost);
			urlConn = (HttpURLConnection) mUrl.openConnection();
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			if (configuration != null){
				urlConn.setConnectTimeout(configuration.getHTTPConnectTimeout());
				urlConn.setReadTimeout(configuration.getWexQueryTimeout());
			}
			urlConn.setRequestMethod("POST");
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			urlConn.setRequestProperty("charset", "utf-8");
			urlConn.setRequestProperty("Content-Length", Integer.toString(postData.length));
			urlConn.setUseCaches(false);
			
			IOUtils.write(postData, urlConn.getOutputStream());
			
			response = getResponse(urlConn.getInputStream());
			
		}catch (SocketTimeoutException timeoutEx){
			logger.error("SocketTimeoutException in doGet()="+timeoutEx.getMessage());
			throw new WexHTTPConnectionException("SocketTimeoutException", timeoutEx); 
		} catch (IOException e) {
			logger.error("IOException in doGet()="+e.getMessage());
			throw new WexHTTPConnectionException("IOException", e);
		}
		finally{
			if(urlConn!=null){
				urlConn.disconnect();
			}
		}

		logger.debug("WexWS Query took "+(System.currentTimeMillis()-t));
		
		return response;
	}
	
	private String getResponse(InputStream is) throws SocketTimeoutException, IOException{
		byte[] responseBytes = IOUtils.toByteArray(is);
		String response = new String(responseBytes, "UTF-8");
		return response;  
	}
}
