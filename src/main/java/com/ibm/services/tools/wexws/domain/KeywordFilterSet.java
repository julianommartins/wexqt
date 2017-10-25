package com.ibm.services.tools.wexws.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class KeywordFilterSet {
	
	public final static String SEPARATOR = ",";
	
	private Set<KeywordFilter> allKeywords;
	private Set<KeywordFilter> mustHaveKeywords;
	private Set<KeywordFilter> niceToHaveKeywords;
	private Set<KeywordFilter> undefinedKeywords;

	public KeywordFilterSet(String keywordString) {
		allKeywords = new HashSet<KeywordFilter>();
		for( String keyword : keywordString.split(SEPARATOR)) {
			if (!keyword.trim().isEmpty()) {
				KeywordFilter newKeywordFilter = new KeywordFilter(keyword);
				allKeywords.add(newKeywordFilter);
			}
		}
	}
	
	public KeywordFilterSet(List<KeywordFilter> allKeywords) {
		this.allKeywords = new HashSet<KeywordFilter>();
		this.allKeywords.addAll(allKeywords);
		groupKeywords();
	}

	public KeywordFilterSet(List<KeywordFilter> mustHaveKeyowrds, List<KeywordFilter> niceToHaveKeywords) {
		this.allKeywords = new HashSet<KeywordFilter>();
		this.allKeywords.addAll(mustHaveKeyowrds);
		this.allKeywords.addAll(niceToHaveKeywords);
		groupKeywords();
	}
	
	public KeywordFilterSet(List<KeywordFilter> mustHaveKeywords, List<KeywordFilter> niceToHaveKeywords, int maxNumberMustHave, int maxNumberNiceToHave) {
		List<KeywordFilter> keywords = new ArrayList<KeywordFilter>();
		keywords.addAll(mustHaveKeywords);
		keywords.addAll(niceToHaveKeywords);
		this.allKeywords = balanceMustHaveNiceToHaveKeywords(keywords, maxNumberMustHave, maxNumberNiceToHave);
		groupKeywords();
	}
	
	public List<KeywordFilter> getAllKeywords() {
		return new ArrayList<KeywordFilter>(allKeywords);
	}
	
	@JsonIgnore
	public String getAllKeywordsAsString() {
		String keys = "";
		int i = 0;
		for (KeywordFilter kw : allKeywords) {
			keys += kw.getKeyword() + ((++i < allKeywords.size())?SEPARATOR:"");
		}
		return keys;

	}

	public List<KeywordFilter> getMustHaveKeywords() {
		return new ArrayList<KeywordFilter>(mustHaveKeywords);
	}
	
	@JsonIgnore
	public String getMustHaveKeywordsAsString() {
		String keys = "";
		int i = 0;
		for (KeywordFilter kw : mustHaveKeywords) {
			keys += kw.getKeyword() + ((++i < mustHaveKeywords.size())?SEPARATOR:"");
		}
		return keys;
	}

	public List<KeywordFilter> getNiceToHaveKeywords() {
		return new ArrayList<KeywordFilter>(niceToHaveKeywords);
	}

	@JsonIgnore
	public String getNiceToHaveKeywordsAsString() {
		String keys = "";
		int i = 0;
		for (KeywordFilter kw : niceToHaveKeywords) {
			keys += kw.getKeyword() + ((++i < niceToHaveKeywords.size())?SEPARATOR:"");
		}
		return keys;
	}

	public List<KeywordFilter> getUndefinedKeywords() {
		return new ArrayList<KeywordFilter>(undefinedKeywords);
	}

	@JsonIgnore
	public String getUndefinedKeywordsAsString() {
		String keys = "";
		int i = 0;
		for (KeywordFilter kw : undefinedKeywords) {
			keys += kw.getKeyword() + ((++i < undefinedKeywords.size())?SEPARATOR:"");
		}
		return keys;
	}
	
	/**
	 * Get keywords from allKeywords and group them into the lists mustHaveKeywords, niceToHaveKeywords, and undefinedKeywords
	 */
	public void groupKeywords() {
		mustHaveKeywords = new HashSet<KeywordFilter>();
		niceToHaveKeywords = new HashSet<KeywordFilter>();
		undefinedKeywords = new HashSet<KeywordFilter>();
		
		for (KeywordFilter keywordFilter : allKeywords) {
			if (KeywordFilterLogic.MUST_HAVE.equals(keywordFilter.getFilterLogic())) {
				mustHaveKeywords.add(keywordFilter);
			} else if (KeywordFilterLogic.NICE_TO_HAVE.equals(keywordFilter.getFilterLogic())) {
				niceToHaveKeywords.add(keywordFilter);
			} else {
				undefinedKeywords.add(keywordFilter);
			}
		}
	}
	
	/**
	 * This method balance the must have and nice to have Keywords using the limits provided
	 * 
	 * @param mustHaveKeywords
	 * @param niceToHaveKeywords
	 * @param maxNumberMustHave
	 * @param maxNumberNiceToHave
	 * @return
	 */
	private Set<KeywordFilter> balanceMustHaveNiceToHaveKeywords(List<KeywordFilter> keywords, int maxNumberMustHave, int maxNumberNiceToHave) {
		Set<KeywordFilter> allKeywords = new HashSet<KeywordFilter>();
		Set<KeywordFilter> result = new HashSet<KeywordFilter>();
		allKeywords.addAll(keywords);

		int mustHaveCount = 0;
		int niceToHaveCount = 0;

		// If not enough must_have, convert some nice_to_have to must_have - DISABLED
		int convertNiceToHave = 0; // (mustHaveKeywords.size()<maxNumberMustHave) ? (maxNumberMustHave - mustHaveKeywords.size()) : 0;

		// max number of -1 means no limit
		maxNumberMustHave = (maxNumberMustHave <= -1) ? Integer.MAX_VALUE : maxNumberMustHave;
		maxNumberNiceToHave = (maxNumberNiceToHave <= -1) ? Integer.MAX_VALUE : maxNumberNiceToHave;

		for (KeywordFilter keyword : allKeywords) {
			keyword.applyDoubleQuotes();

			if (KeywordFilterLogic.MUST_HAVE.equals(keyword.getFilterLogic())) {

				if (mustHaveCount >= maxNumberMustHave) { // if exceeded max must have, convert to nice_to_have
					keyword.setFilterLogic(KeywordFilterLogic.NICE_TO_HAVE);
				}
			} else if (convertNiceToHave > 0) { // If not enough must_have, convert some nice_to_have to must_have
				keyword.setFilterLogic(KeywordFilterLogic.MUST_HAVE);
				convertNiceToHave--;
			} else {
				keyword.setFilterLogic(KeywordFilterLogic.NICE_TO_HAVE);
			}

			if (keyword.isMustHave() && mustHaveCount < maxNumberMustHave) {
				result.add(keyword);
				mustHaveCount++;
			} else if (keyword.isNiceToHave() && niceToHaveCount < maxNumberNiceToHave) {
				result.add(keyword);
				niceToHaveCount++;
			}
		}

		return result;
	}

}
