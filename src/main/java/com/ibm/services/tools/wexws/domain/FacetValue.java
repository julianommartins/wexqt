package com.ibm.services.tools.wexws.domain;

import java.io.Serializable;

public class FacetValue  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String label;
	private Integer count;
	private String selectionState;
	
	public FacetValue(String label, Integer count, String selectionState) {
		super();
		this.label = label;
		this.count = count;
		this.selectionState = selectionState;
	}

	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public Integer getCount() {
		return count;
	}


	public void setCount(Integer count) {
		this.count = count;
	}


	public String getSelectionState() {
		return selectionState;
	}


	public void setSelectionState(String selectionState) {
		this.selectionState = selectionState;
	}
	
	@Override
	public String toString() {
		return "{"+label+" ("+count.toString()+") }";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FacetValue other = (FacetValue) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}
	
	
	
}