package com.ibm.services.tools.wexws.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Stores and classifies the search results counts for a specific keyword combination
 * @author julianom
 *
 */
public class KeywordCombinationResult {
	private KeywordCombination originalCombination;
	private KeywordCombination combination;
	private KeywordCombination missingKeywords;
	private int returnedResults = 0;
	private KeywordCombinationStatus status;
	
	/**
	 * Represent the match quality
	 * GM - good match
	 * TF - too few
	 * TM - too many
	 */
	public static enum KeywordCombinationStatus {
		GM,
		TF, 
		TM
	}
	
	public KeywordCombinationResult(KeywordCombination originalCombination, String combination, int returnedResults, int tooFew, int tooMany) {
		this.originalCombination = originalCombination;
		this.combination = new KeywordCombination(combination);
		this.returnedResults = returnedResults;
		calculateMissingKeywords();
		calculateStatus(tooFew, tooMany);		
	}
	
	private void calculateStatus(int tooFew, int tooMany) {
		if (this.returnedResults > tooMany) {
			this.status = KeywordCombinationStatus.TM;
		} else if (this.returnedResults < tooFew) {
			this.status = KeywordCombinationStatus.TF;
		} else {
			this.status = KeywordCombinationStatus.GM;
		}
	}

	private void calculateMissingKeywords() {	
		List<String> missingKeywords = new ArrayList<String>();
		try{
			missingKeywords.addAll(originalCombination.getKeywordsList());
			missingKeywords.removeAll(combination.getKeywordsList());
		} catch(NullPointerException e) {
			System.out.println("ERROR - Null combination object used to create KeywordCombinationResult");
		}
		
		this.missingKeywords = new KeywordCombination(missingKeywords);
	}

	public KeywordCombination getOriginalCombination() {
		return originalCombination;
	}
	public KeywordCombination getCombination() {
		return combination;
	}
	public KeywordCombination getMissingKeywords() {
		return missingKeywords;
	}
	public int getReturnedResults() {
		return returnedResults;
	}
	public KeywordCombinationStatus getStatus() {
		return status;
	}
	
	public static Comparator<KeywordCombinationResult> COMPARATOR_BY_KEYWORDS_NUMBER_RESULT_COUNT = new Comparator<KeywordCombinationResult>() {
		@Override
		public int compare(KeywordCombinationResult comb1, KeywordCombinationResult comb2) {
		
			//First, by highest number of Keywords
		    int i = Integer.compare(comb2.getCombination().getNumberOfKeywords(), comb1.getCombination().getNumberOfKeywords());
		    if (i != 0) return i;

		    //Then by larger count of results
		    return Integer.compare(comb2.getReturnedResults(), comb1.getReturnedResults());
		}
	};
}
