package com.ibm.services.tools.wexws.customfacets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class BaseFacetMapper implements FacetMapper {

	protected String facetName;
	protected Map<String, Object> arguments;
	
	@Override
	public String getFacetName() {
		return facetName;
	}

	@Override
	public void setFacetName(String name) {
		this.facetName = name;

	}

	@Override
	public Collection<Object> generateSelection(Collection<String> selections) {
		List<Object> nativeSelections = new ArrayList<Object>(selections.size());
		String selectionPrefix = getFacetName()+"==";
		for (String selection: selections){
			if (selection.startsWith(selectionPrefix)) {
				nativeSelections.add(selection);
			}else {
				nativeSelections.add(getFacetName()+"=="+selection);
			}
			
		}
		return nativeSelections;
	}
	
	@Override
	public void setCustomFacetRequestArguments(Map<String, Object> arguments) {
		this.arguments = arguments;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getRequiredParameter(String key) {
		if (!arguments.containsKey(key)) {
			final String message = String.format("Missing required argument: %s", key);
			throw new RuntimeException(message);
		}
		return (T)arguments.get(key);
	}
}
