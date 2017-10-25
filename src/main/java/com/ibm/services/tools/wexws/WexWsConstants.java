package com.ibm.services.tools.wexws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class WexWsConstants {

	public static final String APIRELEASE = "1.6";
	
	public static ArrayList<String> stopWords = new ArrayList<String>(Arrays.asList("im", "know", "prefer", "willing", "broad", "join", "only", "most", "candidate", "held", "week", "after", "beginning", "month", "speak", "starting","company", "offsite", "Seeking", "resource", "required","should","located","ok","also", "need", "site", "looking","responsible","across","i",
			"a","about","an","are","as","at","be","by","com","for","from","how","in","is","of","on","that","the","this","to","was","what","when","where","who","will","with","the","www","a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "describe", "detail", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the"));

	public static final int SNIPPETS_SIZE = 40;
	public static final int MAX_THREADS = 4;
	public static final long TIMEOUT_IN_SECONDS = 30;
	public static final long PARSER_TIMEOUT_IN_SECONDS = 15;
	public static final long TIMEOUT_IN_MILIS = 30000; //60000
	public static final Pattern JAPANESE_QUERY_MODIFIER_PATTERN = Pattern.compile(".+?japanese_context.+?<interpretation>(.+?)</interpretation>.+?");
	public static final Pattern DEFAULT_QUERY_MODIFIER_PATTERN = Pattern.compile(".+?default-context.+?<interpretation>(.+?)</interpretation>.+?");
	public static final String ONTOLECTION_NAME_JP = "PMP_Practitioner_Ontolection_JP";
	public static final String QUERY_MODIFIER_SEPARATOR = " . ";
	
	// URL parameters
	public static final String ONTOLECTION_CONFIGURATION = 
			"<declare name=\"query-expansion.enabled\" /><set-var name=\"query-expansion.enabled\">true</set-var>"+
			"<declare name=\"query-expansion.user-profile\" /><set-var name=\"query-expansion.user-profile\">on</set-var>"+
			"<declare name=\"query-expansion.ontolections\" /><set-var name=\"query-expansion.ontolections\">%s</set-var>"+
			"<declare name=\"query-expansion.max-terms-per-type\" /><set-var name=\"query-expansion.max-terms-per-type\">3</set-var>"+
			"<declare name=\"query-expansion.automatic\" /><set-var name=\"query-expansion.automatic\">synonym:0.8,alternative:0.8,spelling:0.8,narrower:0.5,translation:0.5,broader:0.5,related:0.5</set-var>"+
			"<declare name=\"query-expansion.suggestion\" /><set-var name=\"query-expansion.suggestion\"></set-var>"+
			"<declare name=\"query-expansion.query-match-type\" /><set-var name=\"query-expansion.query-match-type\">terms</set-var>"+
			"<declare name=\"query-expansion.conceptual-search-similarity-threshold\" /><set-var name=\"query-expansion.conceptual-search-similarity-threshold\">0.1</set-var>"+
			"<declare name=\"query-expansion.conceptual-search-metric\" /><set-var name=\"query-expansion.conceptual-search-metric\">euclidean-dot-product</set-var>"+
			"<declare name=\"query-expansion.conceptual-search-candidates-max\" /><set-var name=\"query-expansion.conceptual-search-candidates-max\">euclidean-dot-product</set-var>"+
			"<declare name=\"query-expansion.conceptual-search-sources\" /><set-var name=\"query-expansion.conceptual-search-sources\">%s</set-var>"+
			"<declare name=\"query-expansion.stem-expansions\" /><set-var name=\"query-expansion.stem-expansions\">false</set-var>"+
			"<declare name=\"query-expansion.stemming-dictionary\" /><set-var name=\"query-expansion.stemming-dictionary\">english/wildcard.dict</set-var>"+
			"<declare name=\"reporting.track-spelling\" /><set-var name=\"reporting.track-spelling\">false</set-var>"+
			"<declare name=\"meta.stem-expand-stemmer\" /><set-var name=\"meta.stem-expand-stemmer\">delanguage+english+depluralize</set-var>"+
			"<declare name=\"query-expansion.stemming-weight\" /><set-var name=\"query-expansion.stemming-weight\">0.8</set-var>";
			
	public static final String ONTOLECTION_CONFIGURATION_STEMMING_SPEELING = 
				"<declare name=\"query-expansion.enabled\" /><set-var name=\"query-expansion.enabled\">true</set-var>"+
				"<declare name=\"query-expansion.user-profile\" /><set-var name=\"query-expansion.user-profile\">on</set-var>"+
				"<declare name=\"query-expansion.ontolections\" /><set-var name=\"query-expansion.ontolections\">%s</set-var>"+
				"<declare name=\"query-expansion.max-terms-per-type\" /><set-var name=\"query-expansion.max-terms-per-type\">3</set-var>"+
				"<declare name=\"query-expansion.automatic\" /><set-var name=\"query-expansion.automatic\">synonym:0.8,alternative:0.8,spelling:0.8,narrower:0.5,translation:0.5,broader:0.5,related:0.5</set-var>"+
				"<declare name=\"query-expansion.suggestion\" /><set-var name=\"query-expansion.suggestion\"></set-var>"+
				"<declare name=\"query-expansion.query-match-type\" /><set-var name=\"query-expansion.query-match-type\">terms</set-var>"+
				"<declare name=\"query-expansion.conceptual-search-similarity-threshold\" /><set-var name=\"query-expansion.conceptual-search-similarity-threshold\">0.1</set-var>"+
				"<declare name=\"query-expansion.conceptual-search-metric\" /><set-var name=\"query-expansion.conceptual-search-metric\">euclidean-dot-product</set-var>"+
				"<declare name=\"query-expansion.conceptual-search-candidates-max\" /><set-var name=\"query-expansion.conceptual-search-candidates-max\">euclidean-dot-product</set-var>"+
				"<declare name=\"query-expansion.conceptual-search-sources\" /><set-var name=\"query-expansion.conceptual-search-sources\">%s</set-var>"+
				"<declare name=\"query-expansion.stem-expansions\" /><set-var name=\"query-expansion.stem-expansions\">true</set-var>"+
				"<declare name=\"query-expansion.stemming-dictionary\" /><set-var name=\"query-expansion.stemming-dictionary\">english/wildcard.dict</set-var>"+
				"<declare name=\"reporting.track-spelling\" /><set-var name=\"reporting.track-spelling\">true</set-var>"+
				"<declare name=\"meta.stem-expand-stemmer\" /><set-var name=\"meta.stem-expand-stemmer\">delanguage+english+depluralize</set-var>"+
				"<declare name=\"query-expansion.stemming-weight\" /><set-var name=\"query-expansion.stemming-weight\">0.8</set-var>";
	
	public static final String ONTOLECTION_CONFIGURATION_SPELLING = 
			"<declare name=\"query-expansion.enabled\" /><set-var name=\"query-expansion.enabled\">true</set-var>"+
			"<declare name=\"query-expansion.user-profile\" /><set-var name=\"query-expansion.user-profile\">on</set-var>"+
			"<declare name=\"query-expansion.ontolections\" /><set-var name=\"query-expansion.ontolections\">%s</set-var>"+
			"<declare name=\"query-expansion.max-terms-per-type\" /><set-var name=\"query-expansion.max-terms-per-type\">3</set-var>"+
			"<declare name=\"query-expansion.automatic\" /><set-var name=\"query-expansion.automatic\">synonym:0.8,alternative:0.8,spelling:0.8,narrower:0.5,translation:0.5,broader:0.5,related:0.5</set-var>"+
			"<declare name=\"query-expansion.suggestion\" /><set-var name=\"query-expansion.suggestion\"></set-var>"+
			"<declare name=\"query-expansion.query-match-type\" /><set-var name=\"query-expansion.query-match-type\">terms</set-var>"+
			"<declare name=\"query-expansion.conceptual-search-similarity-threshold\" /><set-var name=\"query-expansion.conceptual-search-similarity-threshold\">0.1</set-var>"+
			"<declare name=\"query-expansion.conceptual-search-metric\" /><set-var name=\"query-expansion.conceptual-search-metric\">euclidean-dot-product</set-var>"+
			"<declare name=\"query-expansion.conceptual-search-candidates-max\" /><set-var name=\"query-expansion.conceptual-search-candidates-max\">euclidean-dot-product</set-var>"+
			"<declare name=\"query-expansion.conceptual-search-sources\" /><set-var name=\"query-expansion.conceptual-search-sources\">%s</set-var>"+
			"<declare name=\"query-expansion.stem-expansions\" /><set-var name=\"query-expansion.stem-expansions\">false</set-var>"+
			"<declare name=\"query-expansion.stemming-dictionary\" /><set-var name=\"query-expansion.stemming-dictionary\">english/wildcard.dict</set-var>"+
			"<declare name=\"reporting.track-spelling\" /><set-var name=\"reporting.track-spelling\">true</set-var>"+
			"<declare name=\"meta.stem-expand-stemmer\" /><set-var name=\"meta.stem-expand-stemmer\">delanguage+english+depluralize</set-var>"+
			"<declare name=\"query-expansion.stemming-weight\" /><set-var name=\"query-expansion.stemming-weight\">0.8</set-var>";
	
	public static final String ONTOLECTION_CONFIGURATION_STEMMING = 
			"<declare name=\"query-expansion.enabled\" /><set-var name=\"query-expansion.enabled\">true</set-var>"+
			"<declare name=\"query-expansion.user-profile\" /><set-var name=\"query-expansion.user-profile\">on</set-var>"+
			"<declare name=\"query-expansion.ontolections\" /><set-var name=\"query-expansion.ontolections\">%s</set-var>"+
			"<declare name=\"query-expansion.max-terms-per-type\" /><set-var name=\"query-expansion.max-terms-per-type\">3</set-var>"+
			"<declare name=\"query-expansion.automatic\" /><set-var name=\"query-expansion.automatic\">synonym:0.8,alternative:0.8,spelling:0.8,narrower:0.5,translation:0.5,broader:0.5,related:0.5</set-var>"+
			"<declare name=\"query-expansion.suggestion\" /><set-var name=\"query-expansion.suggestion\"></set-var>"+
			"<declare name=\"query-expansion.query-match-type\" /><set-var name=\"query-expansion.query-match-type\">terms</set-var>"+
			"<declare name=\"query-expansion.conceptual-search-similarity-threshold\" /><set-var name=\"query-expansion.conceptual-search-similarity-threshold\">0.1</set-var>"+
			"<declare name=\"query-expansion.conceptual-search-metric\" /><set-var name=\"query-expansion.conceptual-search-metric\">euclidean-dot-product</set-var>"+
			"<declare name=\"query-expansion.conceptual-search-candidates-max\" /><set-var name=\"query-expansion.conceptual-search-candidates-max\">euclidean-dot-product</set-var>"+
			"<declare name=\"query-expansion.conceptual-search-sources\" /><set-var name=\"query-expansion.conceptual-search-sources\">%s</set-var>"+
			"<declare name=\"query-expansion.stem-expansions\" /><set-var name=\"query-expansion.stem-expansions\">true</set-var>"+
			"<declare name=\"query-expansion.stemming-dictionary\" /><set-var name=\"query-expansion.stemming-dictionary\">english/wildcard.dict</set-var>"+
			"<declare name=\"reporting.track-spelling\" /><set-var name=\"reporting.track-spelling\">false</set-var>"+
			"<declare name=\"meta.stem-expand-stemmer\" /><set-var name=\"meta.stem-expand-stemmer\">delanguage+english+depluralize</set-var>"+
			"<declare name=\"query-expansion.stemming-weight\" /><set-var name=\"query-expansion.stemming-weight\">0.8</set-var>";

	public static final String BINNING_SELECTION_XML_TEMPLATE = "<term field=\"v.binning-state\" str=\"%s\"/>";
	
	public static final String SYNTAX_OPERATORS = 
			"and or () CONTAINING CONTENT %field%: + NEAR NOT NOTCONTAINING NOTWITHIN OR0 quotes regex stem THRU BEFORE FOLLOWEDBY weight wildcard wildchar WITHIN WORDS site less-than less-than-or-equal greater-than greater-than-or-equal equal range";
	
}
