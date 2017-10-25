package com.ibm.services.tools.wexws.bo;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.ibm.services.tools.wexws.dao.AccessLayerDAO;
import com.ibm.services.tools.wexws.domain.Request;
import com.ibm.services.tools.wexws.domain.Response;
import com.ibm.services.tools.wexws.wql.WQL;

public class QueryBO {

	private static Logger logger = Logger.getLogger(QueryBO.class);
	
	private AccessLayerDAO wexDAO;
	
	public QueryBO(AccessLayerDAO wexRestDAO){
		this.wexDAO = wexRestDAO;
	}

	/**
	 * This method is called from the API (executeQuery)
	 * Given a Request object, trigger WEX queries and provide a Response object
	 * 
	 * @param searchRequest
	 * @return
	 */
	public Response executeQuery(Request searchRequest) {
		Response response;
		try {
			response = wexDAO.executeQuery(searchRequest);
		} catch (Exception e) {
			logger.error("Exception in executeQuery="+e.getMessage());
			response = getResponseWithException(e);
		}
		return response;
	}
	
	/**
	 * Extracts and classify the keywords in a NLQ search query
	 * 
	 * @param searchQuery
	 * @return
	 */
	public String executeQueryParse(String searchQuery) {
		return wexDAO.executeQueryParser(searchQuery);
	}

	/**
	 * Uses the query modifier NLQ tool to extract relevant keywords 
	 * from a Natural Language Query
	 *  
	 * @param nlqStatement
	 * @return
	 */
	public String executeQueryModifier(String nlqStatement) {
		return wexDAO.executeQueryModifier(nlqStatement);
	}
	
	/**
	 * This method is used only for pure WQL searches. 
	 * For requests originated to the API, the method executeQuery(Request) is used instead.
	 * 
	 * @param wqlStatement
	 * @param profile
	 * @param nlq
	 * @param smartCondition
	 * @param ontolection
	 * @param japaneseSearch
	 * @param smartConditionOperator
	 * @param bold
	 * @param stemming
	 * @param spelling
	 * @return
	 */
	public Response executeQuery(String wqlStatement, boolean profile, boolean nlq, boolean smartCondition, boolean ontolection, boolean japaneseSearch, String smartConditionOperator, boolean bold, boolean stemming, boolean spelling) {
		Response response = null;
		
		WQL wql = new WQL(wqlStatement);

		if(!wql.isValid()){
			response = getResponseWithValidationMessage(wql);
		}else{
			response = executeQuery(wql, profile, nlq, smartCondition, ontolection, japaneseSearch, smartConditionOperator, bold, stemming, spelling);
		}
		
		return response;
	}

	
	/**
	 * This method is used only for pure WQL searches. 
	 * For requests originated to the API, the method executeQuery(Request) is used instead.
	 * 
	 * @param wql
	 * @param profile
	 * @param nlq
	 * @param smartCondition
	 * @param ontolection
	 * @param japaneseSearch
	 * @param smartConditionOperator
	 * @param bold
	 * @param stemming
	 * @param spelling
	 * @return
	 */
	private Response executeQuery(WQL wql, boolean profile, boolean nlq, boolean smartCondition, boolean ontolection, boolean japaneseSearch, String smartConditionOperator, boolean bold, boolean stemming, boolean spelling) {
		Response response;
		try {
			response = wexDAO.executeQuery(wql, profile, nlq, smartCondition, ontolection, japaneseSearch, smartConditionOperator, bold, stemming, spelling);
		} catch (Exception e) {
			response = getResponseWithException(e);
		}
		
		return response;
	}
	
	private Response getResponseWithValidationMessage(WQL wql) {
		Response response = new Response(0,null);
		response.setHasError(true);
		response.setValidationMessage((String) wql.getValidationMessage());
		
		return response;
	}

	private Response getResponseWithException(Exception e) {
		Response response = new Response(0,null);
		response.setHasError(true);
		response.setExceptionMessages(Arrays.asList(e.getMessage()));
		
		return response;
	}

}
