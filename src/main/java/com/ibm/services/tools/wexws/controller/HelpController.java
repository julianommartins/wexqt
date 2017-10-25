package com.ibm.services.tools.wexws.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelpController extends HtmlController {

	private final String helpHtml;
	private final String helpFilters;
	private final String helpFacets;
	private final String helpNlq;
	
	public HelpController() throws IOException {
		this.helpHtml = loadHtmlPage("/help.html");
		this.helpFilters = loadHtmlPage("/helpfilters.html");
		this.helpFacets = loadHtmlPage("/facetshelp.html");
		this.helpNlq = loadHtmlPage("/nlq.html");
	}
	
	@RequestMapping("/facetshelp.html")
	public String helpFacets() {
		return this.helpFacets;
	}
	
	/**
	 * Display the help for WQL
	 * @param name
	 * @return
	 */
	@RequestMapping("/help.html")
	public String getHelpHtml() {
		return this.helpHtml;
	}

	/**
	 * Display the help for Filters
	 * @param name
	 * @return
	 */
	@RequestMapping("/helpfilters.html")
	public String getHelpFilters() {
		return this.helpFilters;
	}


	/**
	 * Display the help for NLQ field 
	 * @param name
	 * @return
	 */
	@RequestMapping("/nlq.html")
	public String getHelpNlq() {
		return this.helpNlq;
	}

}
