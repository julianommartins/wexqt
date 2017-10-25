package com.ibm.services.tools.wexws.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Facet  implements Serializable {
	private static final long serialVersionUID = -1489241529840677616L;
	private String name;
	private List<FacetValue> values;
	private int numberOfRequestedValues;
	
	public Facet(String name) {
		this(name,0);
	}

	public Facet(String name, int numberOfRequestedValues) {
		super();
		this.name = name;
		this.numberOfRequestedValues = numberOfRequestedValues;
		this.values = new ArrayList<FacetValue>();
	}
	
	public void addFacetValue(FacetValue value){
		this.values.add(value);	
	}

	public String getName() {
		return name;
	}

	public List<FacetValue> getValues() {
		return values;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		
		sb.append("name:").append(name).append(",\n");
		sb.append("values: [");
		
		int count=0;
		for(FacetValue fv : this.values){
			if(count>0) sb.append(",\n");
			sb.append(fv.toString());
			count++;
		}
		sb.append("]\n");
		
		
		sb.append("}\n");
		
		return sb.toString();
	}

	public int getNumberOfRequestedValues() {
		return numberOfRequestedValues;
	}

	public void setValues(List<FacetValue> values) {
		this.values = values;
	}
	

}
