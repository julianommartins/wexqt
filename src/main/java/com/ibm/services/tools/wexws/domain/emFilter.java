package com.ibm.services.tools.wexws.domain;

import java.util.List;

public class emFilter {
	private String name;
	private List<String> values;

	public emFilter(String name, List<String> values) {
		super();
		this.name = name;
		this.values = values;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}



}
