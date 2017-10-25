package com.ibm.services.tools.wexws.domain;

import com.ibm.services.tools.wexws.customfacets.FacetMapper;

public class CustomFacetRequest extends FacetRequest {

	private final FacetMapper facetMapper;
	
	public CustomFacetRequest(FacetMapper facetMapper){
		super();
		this.facetMapper = facetMapper;
	}
	
	public FacetMapper getFacetMapper(){
		return this.facetMapper;
	}

}
