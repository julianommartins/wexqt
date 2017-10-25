package com.ibm.services.tools.wexws.dao;

import com.ibm.services.tools.wexws.domain.Request;
import com.ibm.services.tools.wexws.domain.Response;
import com.ibm.services.tools.wexws.wql.WQL;

public interface AccessLayerDAO { // WexRestfulDAO implements it
	Response executeQuery(WQL wql, boolean profile, boolean nlq, boolean smartCondition, boolean ontolection, boolean japaneseSearch, String smartConditionOperator, boolean bold, boolean stemming, boolean spelling) throws Exception;
	Response executeQuery(Request searchRequest) throws Exception;
	String executeQueryParser(String searchQuery);
	String executeQueryModifier(String query);
}
