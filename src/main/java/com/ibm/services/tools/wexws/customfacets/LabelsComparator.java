package com.ibm.services.tools.wexws.customfacets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.ibm.services.tools.wexws.domain.FacetValue;

public class LabelsComparator implements Comparator<FacetValue> {
	private List<String> labels;

	public LabelsComparator(Collection<String> collection) {
		this.labels = new ArrayList<String>(collection);
	}

	@Override
	public int compare(FacetValue object1, FacetValue object2) {
		return labels.indexOf(object1.getLabel()) - labels.indexOf(object2.getLabel());
	}
}