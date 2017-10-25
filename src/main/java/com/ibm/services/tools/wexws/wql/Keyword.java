package com.ibm.services.tools.wexws.wql;

public class Keyword {
	
	private String keyword;
	private int position;
	private Keyword nextKeyword;
	
	public Keyword(String keyword, int position) {
		super();
		this.keyword = keyword;
		this.position = position;
	}

	public Keyword getNextKeyword() {
		return nextKeyword;
	}

	public void setNextKeyword(Keyword nextKeyword) {
		this.nextKeyword = nextKeyword;
	}

	public String getKeyword() {
		return keyword;
	}

	public int getPosition() {
		return position;
	}
	
	
	

}
