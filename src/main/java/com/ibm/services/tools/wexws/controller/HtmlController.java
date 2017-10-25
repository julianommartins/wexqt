package com.ibm.services.tools.wexws.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * @author julianom
 *
 */

public abstract class HtmlController {
	
	protected String loadHtmlPage(String filename) throws IOException {
		StringBuilder sb = new StringBuilder();

		InputStream is = getClass().getResourceAsStream(filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		br.close();
		return sb.toString();
	}

}
