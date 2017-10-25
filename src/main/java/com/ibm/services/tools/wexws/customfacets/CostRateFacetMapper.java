package com.ibm.services.tools.wexws.customfacets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ibm.services.tools.wexws.domain.BinningSet;
import com.ibm.services.tools.wexws.domain.Facet;
import com.ibm.services.tools.wexws.domain.Filter;
import com.ibm.services.tools.wexws.helper.WexRequestBuilders;
import com.ibm.services.tools.wexws.utils.XMLUtil;

public class CostRateFacetMapper extends BaseFacetMapper {

	private Map<String, List<Integer>> bucketsPerCurrency;
	private Map<String, String> currencySymbols;
	
	@Override
	public Facet fromNative(List<BinningSet> nativeResponses) {
		CostRateFacetMapperResponseGenerator responseGenerator = new CostRateFacetMapperResponseGenerator(facetName, currencySymbols, nativeResponses);
		return responseGenerator.generate();
	}

	@Override
	public Collection<BinningSet> toNative() {
		String currencyCode = getRequiredParameter(CustomFacetMappersConstants.COST_RATE_PARAMS_CURRENCY_CODE_KEY);
		List<String> costRateFields = getRequiredParameter(CustomFacetMappersConstants.COST_RATE_PARAMS_FIELDS);
		List<Integer> buckets = bucketsPerCurrency.get(currencyCode);

		List<BinningSet> binningSetList = new ArrayList<BinningSet>();
		for (String fieldName: costRateFields){
			for (int bucket : buckets) {
				binningSetList.add(generateBinningRequestForBucket(fieldName, bucket));
			}
		}
		return binningSetList;
	}

	private BinningSet generateBinningRequestForBucket(String fieldName, int bucket) {
		String currencyCode = getRequiredParameter(CustomFacetMappersConstants.COST_RATE_PARAMS_CURRENCY_CODE_KEY);
		Double exchangeRateFromUsDollars = getRequiredParameter(CustomFacetMappersConstants.COST_RATE_PARAMS_EXCHANGE_RATE_FROM_US_DOLLARS);
		
		String xpathExpression = 
				String.format("viv:if-else((($%s * %f) &lt; %d),'%s',%s)",
						fieldName, exchangeRateFromUsDollars, bucket,
						XMLUtil.escapeXML("< " + currencyCode + bucket),
						CustomFacetMappersConstants.AVAILABLE_WITHIN_DUMMY_VALUE_XPATH); 
				
		BinningSet facetRequest = new BinningSet();
		facetRequest.setId(generateIdForFacet(bucket, fieldName, currencyCode));
		facetRequest.setSelectXPath(xpathExpression);
		return facetRequest;
	}
	
	private String generateIdForFacet(int bucket, String fieldName, String currencyCode) {
		StringBuilder idBuilder = new StringBuilder();
		idBuilder.append(facetName).append("-")
		.append(currencyCode).append("-")
		.append(fieldName).append("-").append(bucket);
		return idBuilder.toString();
	}
	
	@Override
	public Collection<Object> generateSelection(Collection<String> selections) {
		Double exchangeRateFromUsDollars = getRequiredParameter(CustomFacetMappersConstants.COST_RATE_PARAMS_EXCHANGE_RATE_FROM_US_DOLLARS);
		List<Filter> orFilters = new ArrayList<Filter>(3);
		
		//The cost rate selection is actually multiple XPath filters, 
		//based on the multiple cost rates fields used to build the facet.
		String xpathFilter = "(($%s * %f) < %s)";
		for (String selection: selections){
			String[] multipleSelections = selection.split("\\|");
			for (String singleSelection: multipleSelections){
				String[] bucketTokens = singleSelection.split("-");
				String fieldName = bucketTokens[2];
				String bucket = bucketTokens[3];
				orFilters.add(WexRequestBuilders.xpathFilter(String.format(xpathFilter, fieldName, exchangeRateFromUsDollars, bucket)));
			}
		}
		Filter orFilter = WexRequestBuilders.orFilter(orFilters.toArray(new Filter[orFilters.size()]));
		Collection<Object> filters = new ArrayList<Object>(1);
		filters.add(orFilter);
		return filters;
	}
	
	public Map<String, List<Integer>> getBucketsPerCurrency() {
		return bucketsPerCurrency;
	}

	public void setBucketsPerCurrency(Map<String, List<Integer>> bucketsPerCurrency) {
		this.bucketsPerCurrency = bucketsPerCurrency;
	}

	public Map<String, String> getCurrencySymbols() {
		return currencySymbols;
	}

	public void setCurrencySymbols(Map<String, String> currencySymbols) {
		this.currencySymbols = currencySymbols;
	}

}
