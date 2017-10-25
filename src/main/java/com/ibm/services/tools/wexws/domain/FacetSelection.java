package com.ibm.services.tools.wexws.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class represent a FACET in a search, example: BAND | 5, 4, 6 | example2: FACET | VALUE 1, VALUE 1 |
 * 
 * @author julianom
 *
 */
public class FacetSelection {
	
	private String name;
	private List<String> selections;
	
	@Override
	public String toString() {
		return name + "=[" + printSelections() + "]";
	}

	public String getName() {
		return name;
	}

	/**
	 * The FACET NAME
	 * 
	 * @param name
	 *            example: BAND, JOB_ROLE
	 */
	public void setName(String name) {
		this.name = name;
	}

	public List<String> getSelections() {
		return selections;
	}
	
	public String printSelections() {
		StringBuilder builder = new StringBuilder();
		if (selections != null) {
			Iterator<String> it = selections.iterator();
			while (it.hasNext()) {
				builder.append("\"");
				builder.append(it.next());
				builder.append("\",");
			}
		}
		return builder.length() > 0 ? builder.substring(0, builder.lastIndexOf(",")): "";
	}

	/**
	 * The possible Values
	 * 
	 * @param values
	 *            Example "8,9,10,Project Manager"
	 */
	
	public void setSelections(List<String> selections) {
        this.selections = selections;
    }
	
	public void addSelection(String selection) {
		this.selections.add(selection);
	}

	public FacetSelection(String name, List<String> selections) {
		super();
		this.name = name;
		this.selections = selections;
	}
	
	public FacetSelection() {
		this.selections = new ArrayList<String>();
	}
	
	public FacetSelection(String name) {
		this.name = name;
		this.selections = new ArrayList<String>();
	}
}
