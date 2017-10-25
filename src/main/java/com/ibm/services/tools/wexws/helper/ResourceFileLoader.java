package com.ibm.services.tools.wexws.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceFileLoader {
	
	private String resourceFilePath;
	
	public ResourceFileLoader(String resourceFilePath) {
		super();
		this.resourceFilePath = resourceFilePath;
	}

	public String getContentsAsString() {
		String resumeText = "";
		BufferedReader br;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			InputStream is = classLoader.getResourceAsStream(resourceFilePath);
			
			br = new BufferedReader(new InputStreamReader(is));
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    resumeText = sb.toString();
		    br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resumeText;
	}

}
