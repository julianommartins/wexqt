package com.ibm.services.tools.wexws.domain;

import java.util.ArrayList;
import java.util.List;

import com.ibm.services.tools.wexws.exception.TooManySearchKeywordsException;

/**
 * Perform combination algorithm to generate a list of keyword combinations
 * @author danigpam
 *
 */
public class KeywordCombinations {
	private List<KeywordCombination> combinations = new ArrayList<KeywordCombination>();

	public List<KeywordCombination> getCombinations() {
		return combinations;
	}

	public KeywordCombinations(KeywordCombination originalKeywordCombination) {
		String arr[] = originalKeywordCombination.getCombination().split(KeywordCombination.SEPARATOR);
		int totalKeywords = arr.length;
		
		if (totalKeywords > 6) {
			throw new TooManySearchKeywordsException(totalKeywords);
		}
		int limit = arr.length;
		if (limit > 0) {
			int r = 1;
			while (r <= limit) {
				createCombination(arr, limit, r);
				r++;
			}
		}
	}

	private void combinationUtil(String arr[], String data[], int start, int end, int index, int r) {
		if (index == r) {
			ArrayList<String> keywordList = new ArrayList<String>();
			for (int j = 0; j < r; j++) {
				if (! data[j].trim().isEmpty()) {
					keywordList.add(data[j]);
				}
			}
			
			if (keywordList.size()>0) {
				combinations.add(new KeywordCombination(keywordList));
			}
			return;
		}

		for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
			data[index] = arr[i];
			combinationUtil(arr, data, i + 1, end, index + 1, r);
		}
	}

	private void createCombination(String arr[], int n, int r) {
		String data[] = new String[r];
		combinationUtil(arr, data, 0, n - 1, 0, r);
	}	
	
}
