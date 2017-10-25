package com.ibm.services.tools.wexws.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ibm.services.tools.wexws.domain.KeywordFilter;

/**
 * This class contains methods to extract keywords based on a given dictionary of keywords
 * 
 * @author RESOARES
 * Aug, 2016
 */
public class CustomKeywordsExtractor {
	
	private static final Logger logger = LogManager.getLogger(CustomKeywordsExtractor.class);
	private Map<String, Collection<String>> keywordsMap= new HashMap<String, Collection<String>>();
	
	
	private static CustomKeywordsExtractor instance;
	
	public static CustomKeywordsExtractor getInstance() {
		if (instance == null) {
			instance = new CustomKeywordsExtractor();
		}
		
		return instance;
	}
	
	private CustomKeywordsExtractor() {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("keyword_map_vV60h_20170914.csv");
		readDict(is);
	}

	/**
	 * Reads a csv file that contains the keywords dictionary and store the keywords in a Map
	 * @param csvFile
	 * 			String csv file path
	 */
	public void readDict(InputStream csvFile){
        BufferedReader br = null;
        String line = "";

        try {
            br = new BufferedReader(new InputStreamReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                String keyw = words[0].trim();
                Set <String> keywList = new TreeSet<String>();
                for(int i=0; i<words.length; i++){
                	if(words[i].trim().length()>0){
                		keywList.add(words[i].toLowerCase().trim());
                	}	
                }
                if(keywordsMap.containsKey(keyw)){
                	keywordsMap.get(keyw).addAll(keywList);
                }else{
                	keywordsMap.put(keyw, keywList);
                }
            }

        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        	logger.debug(csvFile + " was not found! Please check if that is the correct file!");
        	logger.debug("CustomKeywordExtractor threw an Exception: " + e.getMessage(), e);
        	System.exit(1);
        } catch (IOException e) {
        	e.printStackTrace();
        	logger.debug("CustomKeywordExtractor threw an Exception: " + e.getMessage(), e);
        	System.exit(1);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	/**
	 * this method returns the keywords for a given text
	 * 
	 * @param text
	 * 			String text to extract the keywords
	 * 
	 * @return String 
	 * 			keywords comma separated
	 */
	public List<KeywordFilter> findKeywords(String text){

		List<KeywordFilter> keywordList = new ArrayList<KeywordFilter>();
		
		if(text != null && text.length()>0){
			//StringBuilder result = new StringBuilder();
			text = text.trim().toLowerCase().replaceAll(" +", " ");
			List<String> splitTextList = new ArrayList<String>();
			Set<String> resultsList = new HashSet<String>();
			
			String[] splitText = text.split("[\\.\\(\\)\\[\\]\\{\\}\\,\\?\\!\\;\\_\\-\\*\\\"\\'\\s]+");
			splitTextList = Arrays.asList(splitText);
			

			for (Entry<String, Collection<String>> entry : keywordsMap.entrySet()) {
				for (String keyw : entry.getValue()) {
					if((keyw.length()>4 && text.contains(keyw)) || (keyw.length()<=4 && splitTextList.contains(keyw))) {
						resultsList.add(entry.getKey());
					}
				}
			}

			for (String keyw : resultsList) {
				//result.append(keyw).append(",");
				keywordList.add(new KeywordFilter(keyw, null));
			}
		}
		return keywordList;
	}
	
}
