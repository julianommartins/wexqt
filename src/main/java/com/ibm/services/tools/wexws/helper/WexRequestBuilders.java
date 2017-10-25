package com.ibm.services.tools.wexws.helper;

import com.ibm.services.tools.wexws.customfacets.FacetMapper;
import com.ibm.services.tools.wexws.domain.AllTextFieldsFilter;
import com.ibm.services.tools.wexws.domain.AndFilter;
import com.ibm.services.tools.wexws.domain.CustomFacetRequest;
import com.ibm.services.tools.wexws.domain.FacetRequest;
import com.ibm.services.tools.wexws.domain.Filter;
import com.ibm.services.tools.wexws.domain.OrFilter;
import com.ibm.services.tools.wexws.domain.RangeFilter;
import com.ibm.services.tools.wexws.domain.RangeOperator;
import com.ibm.services.tools.wexws.domain.RecordFilter;
import com.ibm.services.tools.wexws.domain.Request;
import com.ibm.services.tools.wexws.domain.TextFilter;
import com.ibm.services.tools.wexws.domain.XPathFilter;


public class WexRequestBuilders {

	public static Request searchRequest(String collectionName){
		return new Request(collectionName);
	}
	
	public static FacetRequest facetRequest(String facetName){
		return new FacetRequest().withFacetName(facetName);
	}
	
	public static FacetRequest customFacetRequest(String facetName, FacetMapper mapper){
		return new CustomFacetRequest(mapper).withFacetName(facetName);
	}
	
	public static Filter textFieldSearch(String fieldName, String textFilter) {
		return new TextFilter(fieldName, textFilter);
	}
	
	public static Filter allTextFieldsSearch(String textFilter) {
		return new AllTextFieldsFilter(textFilter);
	}
	
	public static Filter recordFilter(String fieldName, String value) {
		return new RecordFilter(fieldName, value);
	}
	
	public static Filter xpathFilter(String xPath) {
		return new XPathFilter(xPath);
	}
	
	public static Filter rangeFilter(String fieldName, Number firstValue, Number secondValue, RangeOperator operator) {
		return new RangeFilter(fieldName, firstValue, secondValue, operator);
	}
	
	public static Filter rangeFilter(String fieldName, Number firstValue, RangeOperator operator) {
		return rangeFilter(fieldName, firstValue, null, operator);
	}
	
	public static Filter orFilter(Filter...filters) {
		return new OrFilter(filters);
	}
	
	public static Filter andFilter(Filter...filters) {
		return new AndFilter(filters);
	}
}
