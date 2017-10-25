package com.ibm.services.tools.wexws.configuration;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PartitionFactory {
	
	public static Partition loadFromJson(String json) {
        JSONParser parser = new JSONParser();
        try {
			JSONObject confObject = (JSONObject) parser.parse(json);
			return loadFromJsonObject(confObject);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public static Partition loadFromJsonObject(JSONObject jsonObject) {
		Partition qc = new Partition();
		qc.setDescription((String) jsonObject.get("description"));
		qc.setCriteria((String) jsonObject.get("criteria"));
		qc.setName((String) jsonObject.get("name"));

		qc.setImmediateAvailabilityDays((Long) jsonObject.get("immediate_availability_days"));
		qc.setLateArrivalDays((Long) jsonObject.get("late_arrival_days"));
		qc.setMaxMustHaveKeywords((Long) jsonObject.get("max_must_have_keywords"));
		qc.setMaxNiceToHaveKeywords((Long) jsonObject.get("max_nice_to_have_keywords"));
		qc.setLowBandSlack((Long) jsonObject.get("low_band_slack"));
		qc.setHighBandSlack((Long) jsonObject.get("high_band_slack"));
		
		JSONObject matchingRulesObj = (JSONObject) jsonObject.get("matching_rules");
		qc.setMatchingRules(MatchingRulesFactory.loadFromJsonObject(matchingRulesObj));

		JSONArray jobRetrievedFieldsArray = (JSONArray) jsonObject.get("job_retrieved_fields");
		ArrayList<String> jobRetrievedFields = new ArrayList<String>();
		for (int i = 0; i < jobRetrievedFieldsArray.size(); i++) {
			jobRetrievedFields.add((String) jobRetrievedFieldsArray.get(i));
		}
		qc.setJobRetrievedFields(jobRetrievedFields);
		
		JSONArray personRetrievedFieldsArray = (JSONArray) jsonObject.get("person_retrieved_fields");
		ArrayList<String> personRetrievedFields = new ArrayList<String>();
		for (int i = 0; i < personRetrievedFieldsArray.size(); i++) {
			personRetrievedFields.add((String) personRetrievedFieldsArray.get(i));
		}
		qc.setPersonRetrievedFields(personRetrievedFields);
		
		return qc;
	}
}
