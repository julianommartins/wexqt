package com.ibm.services.tools.wexws.configuration;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MatchingRulesFactory {
	

	public static MatchingRules loadFromJson(String json) {
        JSONParser parser = new JSONParser();
        try {
			JSONObject confObject = (JSONObject) parser.parse(json);
			return loadFromJsonObject(confObject);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public static MatchingRules loadFromJsonObject(JSONObject jsonObject) {
		MatchingRules mr = new MatchingRules();
		mr.setPersonInScope("enabled".equals((String) jsonObject.get("person_in_scope")));
		mr.setJobInScope("enabled".equals((String) jsonObject.get("job_in_scope")));
		mr.setMatchResourceType("enabled".equals((String) jsonObject.get("match_resource_type")));
		mr.setMatchLob("enabled".equals((String) jsonObject.get("match_lob")));
		mr.setMatchWorkLocation("enabled".equals((String) jsonObject.get("match_work_location")));
		mr.setPersonAvailabilityByDate("enabled".equals((String) jsonObject.get("person_availability_by_date")));
		mr.setMatchLateArrival("enabled".equals((String) jsonObject.get("match_late_arrival")));
		mr.setMatchBand("enabled".equals((String) jsonObject.get("match_band")));
		mr.setMatchLanguages("enabled".equals((String) jsonObject.get("match_languages")));
		mr.setMatchMustHaveSkills("enabled".equals((String) jsonObject.get("match_must_have_skills")));
		mr.setMatchNiceToHaveSkills("enabled".equals((String) jsonObject.get("match_nice_to_have_skills")));
			
		return mr;

	}
}
