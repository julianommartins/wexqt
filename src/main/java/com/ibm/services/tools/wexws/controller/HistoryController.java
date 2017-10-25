package com.ibm.services.tools.wexws.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HistoryController extends HtmlController {

	private final String historyHtml;

	public HistoryController() throws IOException {
		this.historyHtml = loadHtmlPage("/history.html");
	}
	
	/**
	 * Read the query history from file history.data and allow user to load, delete or add new queries to the history
	 * @param filter
	 * @return
	 */
	@RequestMapping("/history.html")
	public String gethistoryHtml(@RequestParam(value = "filter", defaultValue = "") String filter) {

		StringBuilder sb = new StringBuilder();

		if (filter == null)
			filter = "";

		String[] filters = null;
		if (filter != null && filter.length() > 0) {
			filters = filter.split(" ");
		}

		int count = 0;
		for (String wql : WQLQueryHistory.getInstance().getHistoryList()) {
			count++;
			try {
				if (matches(wql, filters)) {
					String wql2 = wql.substring(wql.indexOf("SELECT"), wql.length());
					String encoded = URLEncoder.encode(wql2, "UTF-8");
					sb.append("<tr>" + "<td>").append("<a href='/queryToHtml?env=dev&wql=").append(encoded).append("'>").append(count).append("</a><input type='checkbox' name='wqlId' value='").append(count).append("'></td>" + "<td>").append("<a href='/queryToHtml?env=systest&nlq=true&ontolection=true&wql=").append(encoded).append("'>").append(count).append("</a></td>" + "<td><font size=\"1\">").append(wql).append("</font></td></tr>\n");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		String decorated = decorate(sb.toString(), "SELECT", "FIELDS", "FACETS", "FROM", "TEXTSEARCH", "WHERE", "NUM", "SORTBY");

		return this.historyHtml.replace("%filter%", filter).replace("%history%", decorated);
	}
	
	@RequestMapping("/deleteWQLs")
	public synchronized String deleteWQLs(@RequestParam(value = "wqlId") List<Integer> wqlIds, @RequestParam(value = "filterDel", defaultValue = "") String filter) {

		for (Integer id : wqlIds) {
			WQLQueryHistory.getInstance().removeWQLFromHistory(id-1);
		}

		WQLQueryHistory.getInstance().updateHistory();

		return gethistoryHtml(filter);
	}

	private boolean matches(String wql, String[] filters) {
		if (filters == null)
			return true;
		int count = 0;
		for (String filter : filters) {
			if (wql.toLowerCase().indexOf(filter.trim().toLowerCase()) > -1) {
				count++;
			}
		}
		return (filters.length == count);
	}
	
	private String decorate(String s, String... keys) {
		for (String k : keys) {
			s = s.replace(k + " ", "<div class=\"key-word\">" + k + "</div> ").replace(k + "\n", "<div class=\"key-word\">" + k + "</div> ");

		}
		return s;
	}
		
}
