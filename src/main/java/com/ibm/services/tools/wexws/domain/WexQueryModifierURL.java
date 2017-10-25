package com.ibm.services.tools.wexws.domain;

import com.ibm.services.tools.wexws.WexConfiguration;

public class WexQueryModifierURL extends WexBaseURL {

	private String searchQuery;
	private String context;
	
	public WexQueryModifierURL(String server, WexConfiguration configuration, String context, String searchQuery) {
		super(server, configuration);
		this.searchQuery = searchQuery;
		this.context = context;
	}

	public String getQueryModifierURL(){
		StringBuilder sb = new StringBuilder();
		appendBasicServerParameters(sb);
		sb.append("&v.function=query-parse-querymodifier&query-string="+encodeValue(searchQuery));
		if (context.length()>1){
			sb.append("&context=").append(encodeValue(context));
		}
		return sb.toString();
	}
}
