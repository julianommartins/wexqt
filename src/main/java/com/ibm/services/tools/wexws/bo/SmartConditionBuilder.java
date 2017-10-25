package com.ibm.services.tools.wexws.bo;

public class SmartConditionBuilder {

	@SuppressWarnings("unused")
	private final SmartConditionDataProvider dataProvider;
	
	public SmartConditionBuilder(SmartConditionDataProvider dataProvider){
		this.dataProvider = dataProvider;
	}

	public String getSmartConditionsXPath(String textSearch, String smartConditionOperator, String IOT){
		try {
			String smartConditions = "(";
			

			if (smartConditions.length() > 1) {
				smartConditions += ")";
			} else {
				smartConditions = "";
			}

			
			
			return smartConditions;
		} catch (Exception ex){
			System.out.println("Error building Smart Condition: " + ex.getMessage());
			return "";
		}
		
	}
	
}
