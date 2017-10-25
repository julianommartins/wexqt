package com.ibm.services.tools.wexws.customfacets;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ibm.services.tools.wexws.domain.BinningSet;
import com.ibm.services.tools.wexws.domain.Facet;

public interface FacetMapper {

	public String getFacetName();
	
	public void setFacetName(String name);
	
	/**
	 *Sets the argumentsProvider for this mapper  
	 */
	 public void setCustomFacetRequestArguments(Map<String, Object> arguments);
	
	/**
	 * Receives a list of Response facets and removes from the list the facets that this
	 * instances maps to an Access Layer FacetResponse and creates the access layer response.
	 * If it doesn't find matching response it will return null.  
	 * @param nativeResponses
	 * @return a newly created facet response
	 */
	public Facet fromNative(List<BinningSet> nativeResponses);
		
	
	/**
	 * Maps an Access Layer facet request to potentially multiple native facet requests
	 * @param arguments
	 * @return a sequence of NativeFacetRequests
	 */
	public Collection<BinningSet> toNative();
	
	/**
	 * 
	 * @param selectedIds 
	 * @return A sequence of query objects in the native format used to select this facet
	 */
	public Collection<Object> generateSelection(Collection<String> selections);
	
}

