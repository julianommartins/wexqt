package com.ibm.services.tools.wexws.domain;

import java.io.Serializable;

public class Field  implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String value;
	
	public Field(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
		
	@Override
	public String toString() {
		return "{".concat(name).concat(":").concat(value).concat("}");
	}
}
