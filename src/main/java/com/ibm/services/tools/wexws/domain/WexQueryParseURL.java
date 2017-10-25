package com.ibm.services.tools.wexws.domain;

import com.ibm.services.tools.wexws.WexConfiguration;
import com.ibm.services.tools.wexws.WexWsConstants;

public class WexQueryParseURL extends WexBaseURL {
	private String searchQuery;
	
	public WexQueryParseURL(String server, WexConfiguration configuration, String searchQuery) {
		super(server, configuration);
		this.searchQuery = searchQuery;
	}

	public String getQueryParseURL(){
		StringBuilder sb = new StringBuilder();
		appendBasicServerParameters(sb);
		sb.append("&v.function=query-parse&query=").append(encodeValue(searchQuery));
		sb.append("&supported-operators=").append(encodeValue(WexWsConstants.SYNTAX_OPERATORS));
		return sb.toString();
	}
}
