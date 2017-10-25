package com.ibm.services.tools.wexws.customfacets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ibm.services.tools.wexws.domain.Bin;
import com.ibm.services.tools.wexws.domain.BinningSet;
import com.ibm.services.tools.wexws.domain.Facet;
import com.ibm.services.tools.wexws.domain.FacetValue;

public class SimpleFacetMapperResponseGenerator {
	private final String facetName;
	private final List<BinningSet> nativeResponses;
	private Comparator<FacetValue> comparator;

	public SimpleFacetMapperResponseGenerator(String facetName, List<BinningSet> nativeResponses,Comparator<FacetValue> comparator) {
		this.facetName = facetName;
		this.nativeResponses = nativeResponses;
		this.comparator = comparator; 
	}
	
	public SimpleFacetMapperResponseGenerator(String facetName, List<BinningSet> nativeResponses) {
		this(facetName,nativeResponses,null);
	}


	public Facet generate() {
		List<BinningSet> myFacets = findMyFacets();
		nativeResponses.removeAll(myFacets);
		return createResponseFromMyFacets(myFacets);
	}

	private Facet createResponseFromMyFacets(List<BinningSet> myFacets) {
		List<FacetValue> values = new ArrayList<FacetValue>();
		for (BinningSet facet : myFacets) {
			if (facet.getBins() != null){
				for (Bin value : facet.getBins()) {
					if (!CustomFacetMappersConstants.DUMMY_FACET_VALUE.equals(value.getLabel())) {
						values.add(new FacetValue(value.getLabel(), value.getNdocs(), value.getLabel()));
					}
				}
			}
			
		}
		Facet facet = new Facet(facetName);
		facet.setValues(sortValues(values));
		return facet;
	}

	private List<FacetValue> sortValues(List<FacetValue> values) {
		if (comparator != null) {
			Collections.sort(values, comparator);
		}
		return values;
	}

	private List<BinningSet> findMyFacets() {
		List<BinningSet> facets = new ArrayList<BinningSet>();
		for (BinningSet binningSet: nativeResponses){
			if (facetName.equals(binningSet.getId())){
				facets.add(binningSet);
			}
		}
		return facets;
	}
}
