package com.ibm.services.tools.wexws.customfacets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.services.tools.wexws.domain.Bin;
import com.ibm.services.tools.wexws.domain.BinningSet;
import com.ibm.services.tools.wexws.domain.Facet;
import com.ibm.services.tools.wexws.domain.FacetValue;

public class CostRateFacetMapperResponseGenerator {

	private static final String regexPattern = "^%s-([A-Z]+)(-[^-]+)+-(\\d+)$";
	private final String facetName;
	private final Map<String, String> currencySymbols;
	private final List<BinningSet> nativeResponses;
	private final Pattern pattern;
		
	public CostRateFacetMapperResponseGenerator(String facetName,
			Map<String, String> currencySymbols, List<BinningSet> nativeResponses) {
		this.facetName = facetName;
		this.currencySymbols = currencySymbols;
		this.nativeResponses = nativeResponses;
		pattern = Pattern.compile(String.format(regexPattern, facetName));
	}
	
	public Facet generate() {
		Collection<BinningSet> myFacets = findMyFacets();
		nativeResponses.removeAll(myFacets);
		Collection<FacetValue> values = convertToFacetResponseValues(findFacetsWithValues(myFacets));
		Facet facet = new Facet(facetName);
		facet.setValues(mergeFacetResponseValues(values));
		return facet;
	}

	private Collection<BinningSet> findFacetsWithValues(Collection<BinningSet> myFacets) {
		List<BinningSet> facetsWithValues = new ArrayList<BinningSet>(myFacets.size());
		for (BinningSet binningSet: myFacets){
			if (binningSet.getBins() != null && binningSet.getBins().size() > 1){
				facetsWithValues.add(binningSet);
			}
		}
		return facetsWithValues;
	}

	private List<FacetValue> mergeFacetResponseValues(Collection<FacetValue> values){
		Map<String, FacetValue> mergedValues = new LinkedHashMap<String, FacetValue>();
		for (FacetValue value: values){
			FacetValue mergedValue = mergedValues.get(value.getLabel());
			if (mergedValue == null){
				mergedValues.put(value.getLabel(), value);
			}else{
				mergedValue.setCount(mergedValue.getCount() + value.getCount());
				mergedValue.setSelectionState(mergedValue.getSelectionState() + "|" + value.getSelectionState());
			}
		}
		return new ArrayList<FacetValue>(mergedValues.values());
	}
	
	private String generateLabel(String currencyCode, int bucket) {
		return String.format("< %s%d", currencySymbols.get(currencyCode),
				bucket);
	}

	private Collection<FacetValue> convertToFacetResponseValues(Collection<BinningSet> myFacets) {
		List<FacetValue> facetValues = new ArrayList<FacetValue>();
		for (BinningSet binningSet: myFacets){
			Matcher m = pattern.matcher(binningSet.getId());
	 		m.matches();
			String currencyCode = m.group(1);
			int bucket = Integer.parseInt(m.group(3));
			
			int numberOfMatches = 0;
			for (Bin value: binningSet.getBins()){
				if (!value.getLabel().equals(CustomFacetMappersConstants.DUMMY_FACET_VALUE)){
					numberOfMatches = value.getNdocs();
					break;
				}
			}
					
			final String label = generateLabel(currencyCode, bucket);
			facetValues.add(new FacetValue(label, numberOfMatches, binningSet.getId()));
		}
		return facetValues;
	}

	private Collection<BinningSet> findMyFacets() {
		List<BinningSet> costRateFacets = new ArrayList<BinningSet>();
		for (BinningSet binningSet: nativeResponses){
			if (pattern.matcher(binningSet.getId()).matches()){
				costRateFacets.add(binningSet);
			}
		}
		return costRateFacets;
	}
}
