package com.ibm.services.tools.wexws.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeywordClassification {
	
	private String originalQuery;
	private KeywordFilterSet keywords;
	private boolean isMustHaveExplicit;
	
	public KeywordClassification(String originalQuery, String allKeywords) {
		this.keywords = new KeywordFilterSet(allKeywords);
		this.originalQuery = originalQuery;
		prepareOriginalQuery();
		classifyKeywords();
		this.keywords.groupKeywords();
		this.originalQuery = originalQuery;
		updateKeywordSpelling();
	}

	/**
	 * Change keyword spelling in original query to match keywords found by WEX
	 */
	private void prepareOriginalQuery() {
		for (KeywordFilter keywordFilter : keywords.getAllKeywords()) {
			int startKeyword = originalQuery.toLowerCase().indexOf(keywordFilter.getKeyword().toLowerCase());
			if (startKeyword > -1) {
				String originalKeyword = originalQuery.substring(startKeyword, startKeyword+keywordFilter.getKeyword().length());
				originalQuery = originalQuery.replace(originalKeyword, keywordFilter.getKeyword());
			}
		}
	}
	
	/**
	 * Change keywords spelling to match the original query 
	 */
	private void updateKeywordSpelling() {
		for (KeywordFilter keywordFilter : keywords.getAllKeywords()) {
			int startKeyword = originalQuery.toLowerCase().indexOf(keywordFilter.getKeyword().toLowerCase());
			if (startKeyword > -1) {
				String originalKeyword = originalQuery.substring(startKeyword, startKeyword+keywordFilter.getKeyword().length());
				keywordFilter.setKeyword(originalKeyword);
			}
		}
	}

	/**
	 * Classifies all keywords based on the original Wql <br>
	 * Each keyword on allKeywords list will be classified as: <br><br>
	 * KeywordFilterLogic.MUST_HAVE - if indicated by the original Wql <br>
	 * KeywordFilterLogic.NICE_TO_HAVE - if indicated by the original Wql <br>
	 * Null - if not able to indicate from the original Wql <br>
	 * @param originalQuery
	 */
	private void classifyKeywords() {
		isMustHaveExplicit = isMustHave(originalQuery);
		originalQuery = originalQuery.replaceAll("\n", ". ").replaceAll("\r", ". ").replaceAll("\t", ". ").replaceAll(" \\.", ". .");
		classifyKeywords(originalQuery.split(",|\\. "));
	}

	/**
	 * Classifies allKeywords list based on a list of searchPieces extracted from original WQL <br>
	 * Each keyword on allKeywords list will be classified as: <br><br>
	 * KeywordFilterLogic.MUST_HAVE - if the search pieces indicate so <br>
	 * KeywordFilterLogic.NICE_TO_HAVE - if the search pieces indicate so <br>
	 * Null - if not indicated by any search piece <br>
	 * @param searchPieces
	 */
	private void classifyKeywords(String[] searchPieces) {
		
		KeywordFilterLogic last = KeywordFilterLogic.MUST_HAVE;
		
		for (String searchPiece : searchPieces) {
			
			//isMustHaveExplicit will enable us to split the string properly even if don't have an explicit must have identifier
			//Potentially, every non-NiceToHave will be must have
			if ((isMustHave(searchPiece) || !isMustHaveExplicit) && isNiceToHave(searchPiece)) {
				if (searchPiece.contains("and")) {
					classifyKeywords(searchPiece.split("and"));
					continue;
				}
			}
			
			for (KeywordFilter kw : keywords.getAllKeywords()) {
				
				if (searchPieceContainsKeyword(searchPiece, kw)) {
					if (isMustHave(searchPiece)) {
						kw.setFilterLogic(KeywordFilterLogic.MUST_HAVE);
						last = KeywordFilterLogic.MUST_HAVE;
						
					} else if (isNiceToHave(searchPiece) && !isClassified(kw)) {
						kw.setFilterLogic(KeywordFilterLogic.NICE_TO_HAVE);
						last = KeywordFilterLogic.NICE_TO_HAVE;
				
					} else if(!isClassified(kw)) {
						if (last.equals(KeywordFilterLogic.MUST_HAVE)) {
							kw.setFilterLogic(KeywordFilterLogic.MUST_HAVE);
						} else {
							kw.setFilterLogic(KeywordFilterLogic.NICE_TO_HAVE);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Identify if the searchPiece contains the keyword text - include a validation for whole word search and multiple-word keywords
	 * @param searchPiece
	 * @param keyword
	 * @return
	 */
	private boolean searchPieceContainsKeyword(String searchPiece, KeywordFilter keyword) {
		searchPiece = searchPiece.replaceAll("\\.", " ").trim().toLowerCase();
		String keywordText = keyword.getKeyword().replaceAll("\\.", " ").trim().toLowerCase();
		
		if (!keywordText.contains(" ")) {
			//String searchPieceWithNoFinalDot = searchPiece.replaceAll("\\. *$", "").toLowerCase();
			List<String> wordsInSearchPiece = new ArrayList<String>(Arrays.asList(searchPiece.split(" ")));
			return wordsInSearchPiece.contains(keywordText);
		}
		return searchPiece.contains(keywordText);
	}
	
	
	
	private boolean isNiceToHave(String searchPiece) {
		if (searchPiece.toLowerCase().contains("nice to have") 
				|| searchPiece.toLowerCase().contains("optional") 
				|| searchPiece.toLowerCase().contains("would be nice") 
				|| searchPiece.toLowerCase().contains("plus")
				|| searchPiece.toLowerCase().contains("bonus")
				|| searchPiece.toLowerCase().contains("optional")
				|| searchPiece.toLowerCase().contains("desired")) {
			return true;
		}
		return false;
	}

	private boolean isMustHave(String searchPiece) {
		if (searchPiece.toLowerCase().contains("must have") 
				|| searchPiece.toLowerCase().contains("required")
				|| searchPiece.toLowerCase().contains("requires")
				|| searchPiece.toLowerCase().contains("need to have")) {
			return true;
		}
		return false;
	}
	
	private boolean isClassified(KeywordFilter keyword) {
		return keyword.getFilterLogic().equals(KeywordFilterLogic.MUST_HAVE) || keyword.getFilterLogic().equals(KeywordFilterLogic.NICE_TO_HAVE);
	}

	public List<KeywordFilter> getAllKeywords() {
		return keywords.getAllKeywords();
	}
	
	public String getAllKeywordsAsString() {
		return keywords.getAllKeywordsAsString();
	}

	public List<KeywordFilter> getMustHaveKeywords() {
		return keywords.getMustHaveKeywords();
	}
	
	public String getMustHaveKeywordsAsString() {
		return keywords.getMustHaveKeywordsAsString();
	}

	public List<KeywordFilter> getNiceToHaveKeywords() {
		return keywords.getNiceToHaveKeywords();
	}

	public String getNiceToHaveKeywordsAsString() {
		return keywords.getNiceToHaveKeywordsAsString();
	}

	public List<KeywordFilter> getUndefinedKeywords() {
		return keywords.getUndefinedKeywords();
	}

	public String getUndefinedKeywordsAsString() {
		return keywords.getUndefinedKeywordsAsString();
	}
	
	public KeywordFilterSet getKeywords() {
		return keywords;
	}

	public void setKeywords(KeywordFilterSet keywords) {
		this.keywords = keywords;
	}

	public String getOriginalQuery() {
		return originalQuery;
	}
}
