package com.ibm.services.tools.wexws.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.services.tools.wexws.bo.QueryBO;
import com.ibm.services.tools.wexws.domain.Response;
import com.ibm.services.tools.wexws.domain.WQLResponse;
import com.ibm.services.tools.wexws.factory.WexRestfulFactory;

@RestController
public class QueryController extends HtmlQueryController {

	private final String htmlTemplate;

	public QueryController() throws IOException {
		this.htmlTemplate = loadHtmlPage("/query.html");
	}

	@RequestMapping(path = {"/query.html", "/"})
	public String getQueryHtml(
			@RequestParam(value = "name", defaultValue = "World") String name) {
		return this.htmlTemplate.replaceAll("%wql%", "")
				.replaceAll("%response%", "").replaceAll("%message%", "")
				.replace("%restfulRequests%", "").replace("%profile%", "")
				.replace("%token%", "").replace("%stemming%", "")
				.replace("%spelling%", "");
	}

	@RequestMapping("/queryToHtml")
	public String queryToHtml(
			@RequestParam(value = "env", defaultValue = "dev") String environment,
			@RequestParam(value = "profile", required = false) String profile,
			@RequestParam(value = "genjson", required = false) String genjson,
			@RequestParam(value = "nlq", required = false) String nlq,
			@RequestParam(value = "smartCondition", required = false) String smartCondition,
			@RequestParam(value = "ontolection", required = false) String ontolection,
			@RequestParam(value = "stemming", required = false) String stemming,
			@RequestParam(value = "spelling", required = false) String spelling,
			@RequestParam(value = "japanese", required = false) String japanese,
			@RequestParam(value = "smartConditionOperator", defaultValue = "and") String smartConditionOperator,
			@RequestParam(value = "wql", defaultValue = "SELECT \nFIELDS field-list \nFACETS facet-list \nFROM collection") String wql,
			@RequestParam(value = "token", defaultValue = "") String token,
			@RequestParam(value = "printResume", defaultValue = "", required = false) String printResume,
			@RequestParam(value = "printScore", defaultValue = "", required = false) String printScore) {
		WQLQueryHistory.getInstance().saveHistory(wql);
		// tem que limpar virgula aqui, se tiver textsearch
		String cleanWQL = wql.replaceAll("\n", " ").replaceAll("\r", " ")
				.replaceAll("\t", " ").replaceAll("\\.", "");// .replaceAll("&",
																// "%26");
		WQLResponse wqlResponse = new WQLResponse();
		wqlResponse.setWql(wql);

		String outcome = this.htmlTemplate.replace("%wql%", wql);

		try {
			QueryBO querybo = new QueryBO(
					WexRestfulFactory.getInstance(environment));
			System.out.println("CleanWQL->" + cleanWQL);
			Response response = querybo.executeQuery(cleanWQL,
					(profile != null), (nlq != null), (smartCondition != null),
					(ontolection != null), (japanese != null),
					smartConditionOperator, true, (stemming != null),
					(spelling != null));

			if (genjson != null) {
				String responseHtml = getJson(response);
				outcome = responseHtml;
			} else {
				String responseHtml = getHtml(response, true, false, false,
						false);

				outcome = outcome.replace("%response%", responseHtml);

				outcome = outcome.replace("%selected-" + environment + "%",
						"selected='selected'");

				String message = "";

				if (response.getValidationMessage() != null) {
					message = response.getValidationMessage();
				}

				if (response.getExceptionMessages() != null) {
					message = message.concat("<br>").concat(
							response.getExceptionMessages().toString());
				}

				outcome = outcome.replace("%message%", message);

				String restfulRequests = getRestfulRequestsHtml(response);

				outcome = outcome.replace("%restfulRequests%", restfulRequests);

				if (profile != null) {
					outcome = outcome.replace("%profile%", "checked");
				}

				if (printResume != null) {
					outcome = outcome.replace("%printResume%", "checked");
				}

				if (nlq != null) {
					outcome = outcome.replace("%nlq%", "checked");
				}
				if (japanese != null) {
					outcome = outcome.replace("%japanese%", "checked");
				}

				if (smartCondition != null) {
					outcome = outcome.replace("%smartCondition%", "checked");
				}

				if (ontolection != null) {
					outcome = outcome.replace("%ontolection%", "checked");
				}

				if (stemming != null) {
					outcome = outcome.replace("%stemming%", "checked");
				}

				if (spelling != null) {
					outcome = outcome.replace("%spelling%", "checked");
				}

				if (token != null) {
					outcome = outcome.replace("%token%", token);
				}

				outcome = outcome.replace("%smartConditionOperator%",
						smartConditionOperator);

			}
		} catch (Exception e) {
			e.printStackTrace();
			outcome = outcome.replace("%response%", e.getMessage());

		}

		return outcome;
	}

}
