package com.ibm.services.tools.wexws.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * Represents the Keywords combination object
 * @author danigpam
 *
 */
public class KeywordCombination {
	
	public final static String SEPARATOR = ",";
	
	private List<String> keywordsList;
	private String combinationString;


	public KeywordCombination(List<String> keywordsList) {	
		this.keywordsList = keywordsList;
	}

	public KeywordCombination(String combinationString) {
		this.combinationString = combinationString;
		this.keywordsList = new ArrayList<String>(Arrays.asList(combinationString.split(SEPARATOR)));
	}

	public String getCombination() {
		if (combinationString == null) {
			combinationString = StringUtils.collectionToDelimitedString(keywordsList, SEPARATOR);
		}
		return combinationString;
	}

	public int getNumberOfKeywords() {
		return keywordsList.size();
	}

	public List<String> getKeywordsList() {
		return keywordsList;
	}
	
	@Override
	public String toString() {
		return "KeywordsCombination [" + getCombination() + "]";
	}
}
