package com.ibm.services.tools.wexws.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.ibm.services.tools.wexws.WexWsConstants;

public class Document implements Comparable<Document>{
	
	private String vseKey;
	private float score;
	
	private List<Content> contents;
	private List<DocumentSortKey> sortKeys;
	

	public String getVseKey() {
		return vseKey;
	}

	@XmlAttribute(name="vse-key")
	public void setVseKey(String vseKey) {
		this.vseKey = vseKey;
	}

	public float getScore() {
		return score;
	}

	@XmlAttribute(name="score")
	public void setScore(float score) {
		this.score = score;
	}



	public List<Content> getContents() {
		return contents;
	}

	@XmlElement(name="content")
	public void setContents(List<Content> contents) {
		this.contents = contents;
	}
	
	/**
	 * Return the number of keywords found in the Document
	 * Just search in the requested field. If WEx found in a not requested field, will not show accurate results
	 * @param keywords
	 * @return
	 */
	public int getNumKeyFound(List <String> keywords){
		int found = 0;
		try{
			if (null != keywords){
				for (String string : keywords) {
//					if (string.equalsIgnoreCase("AT&T")){
//						string = "at&amp;t";
//					}
					for (Content content : contents){
						//if (content.getName().equalsIgnoreCase("RESUME_TEXT") || content.getName().equalsIgnoreCase("SKILLS")){
							if (content.getValue().toLowerCase().contains(string.toLowerCase())){
								found++;
								break;
							}
						//}
					}	
				}	
			}
		} catch (Exception ex){
			System.out.println("Error at getNumKeyFound:" + ex.getMessage());
		}
		return found;
	}
	
	public int getNumKeyFoundWeb(List <String> keywords){
		int found = 0;
		try{
			if (null != keywords){
				for (String string : keywords) {
					if (string.equalsIgnoreCase("AT&T")){
						string = "at&amp;t";
					}
					for (Content content : contents){
						//if (content.getName().equalsIgnoreCase("RESUME_TEXT") || content.getName().equalsIgnoreCase("SKILLS")){
							if (content.getValue().toLowerCase().contains(string.toLowerCase())){
								found++;
								break;
							}
						//}
					}	
				}	
			}
		} catch (Exception ex){
			System.out.println("Error at getNumKeyFound:" + ex.getMessage());
		}
		return found;
	}
	
	/**
	 * Return the number of keywords found in the Document, considering repetable keys
	 * Just search in the requested field. If WEx found in a not requested field, will not show accurate results
	 * @param keywords
	 * @return
	 */
	public int getNumKeyFoundTotal(List <String> keywords){
		int found = 0;
		try{
			if (null != keywords){
				for (String string : keywords) {
//					if (string.equalsIgnoreCase("AT&T")){
//						string = "at&amp;t";
//					}
					for (Content content : contents){
						//if (content.getName().equalsIgnoreCase("RESUME_TEXT") || content.getName().equalsIgnoreCase("SKILLS")){
							if (content.getValue().toLowerCase().contains(string.toLowerCase())){
								found += org.springframework.util.StringUtils.countOccurrencesOf(content.getValue().toLowerCase(), string.toLowerCase());
								//found++;
							}
						//}
					}	
				}	
			}
		} catch (Exception ex){
			System.out.println("Error at getNumKeyFound:" + ex.getMessage());
		}
		return found;
	}
	
	/**
	 * Return the keywords found in the Document
	 * Just search in the requested field. If WEx found in a not requested field, will not show accurate results
	 * @param keywords
	 * @return
	 */
	public List <String> getKeyFound(List <String> keywords){
		List<String> keysFound = new ArrayList<String>();
		try {
			if (null != keywords) {
				for (String string : keywords) {
					string = string.replaceAll("^\"", "");
					string = string.replaceAll("\"$", "");
					
//					if (string.equalsIgnoreCase("AT&T")){
//						string = "at&amp;t";
//					}
					for (Content content : contents) {
						// if (content.getName().equalsIgnoreCase("RESUME_TEXT") || content.getName().equalsIgnoreCase("SKILLS")){
						if (content.getValue().toLowerCase().contains(string.toLowerCase())) {
							keysFound.add(string);
							break;
						}
						// }
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Error at getKeyFound:" + ex.getMessage());
		}
		return keysFound;
	}
	
	public int getNumKeyFoundTotalWeb(List <String> keywords){
		int found = 0;
		try{
			if (null != keywords){
				for (String string : keywords) {
					if (string.equalsIgnoreCase("AT&T")){
						string = "at&amp;t";
					}
					for (Content content : contents){
						//if (content.getName().equalsIgnoreCase("RESUME_TEXT") || content.getName().equalsIgnoreCase("SKILLS")){
							if (content.getValue().toLowerCase().contains(string.toLowerCase())){
								found += org.springframework.util.StringUtils.countOccurrencesOf(content.getValue().toLowerCase(), string.toLowerCase());
								//found++;
							}
						//}
					}	
				}	
			}
		} catch (Exception ex){
			System.out.println("Error at getNumKeyFound:" + ex.getMessage());
		}
		return found;
	}
	
	public List <String> getKeyFoundWeb(List <String> keywords){
		List<String> keysFound = new ArrayList<String>();
		try {
			if (null != keywords) {
				for (String string : keywords) {
					if (string.equalsIgnoreCase("AT&T")){
						string = "at&amp;t";
					}
					for (Content content : contents) {
						// if (content.getName().equalsIgnoreCase("RESUME_TEXT") || content.getName().equalsIgnoreCase("SKILLS")){
						if (content.getValue().toLowerCase().contains(string.toLowerCase())) {
							keysFound.add(string);
							break;
						}
						// }
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Error at getKeyFound:" + ex.getMessage());
		}
		return keysFound;
	}
	
	/**
	 * Return the keywords found in the Document with the corresponding Field
	 * @param keywords
	 * @return
	 */
	public List <String> getKeyFoundandFields(List <String> keywords){
		List<String> keysFound = new ArrayList<String>();
		try {
			if (null != keywords) {
				for (String string : keywords) {
//					if (string.equalsIgnoreCase("AT&T")){
//						string = "at&amp;t";
//					}
					for (Content content : contents) {
						// if (content.getName().equalsIgnoreCase("RESUME_TEXT") || content.getName().equalsIgnoreCase("SKILLS")){
						if (content.getValue().toLowerCase().contains(string.toLowerCase())) {
							keysFound.add(string + "-" + content.getName());
						}
						// }
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Error at getKeyFound:" + ex.getMessage());
		}
		return keysFound;
	}
	
	public List <String> getKeyFoundandFieldsWeb(List <String> keywords){
		List<String> keysFound = new ArrayList<String>();
		try {
			if (null != keywords) {
				for (String string : keywords) {
					if (string.equalsIgnoreCase("AT&T")){
						string = "at&amp;t";
					}
					for (Content content : contents) {
						if (content.getValue().toLowerCase().contains(string.toLowerCase())) {
							if (!keysFound.contains(string + "-" + content.getName()) ){
								keysFound.add(string + "-" + content.getName());
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Error at getKeyFound:" + ex.getMessage());
		}
		return keysFound;
	}
	
	/**
	 * This is the new method that find keywords always considering the REQUIRED words as found, avoiding search it
	 * @param keywords
	 * @return
	 */
	public List <String> getKeyFound(List <String> keywords, List <String> keywordsRequired, List <String> keywordsOptional, Set<SearchKeywordResponse> searchKeywordResponseList){
		List<String> keysFound = new ArrayList<String>();

		try {
			// In the first query, the Required and Not Required will be empty/null
			 if ((null == keywordsRequired || keywordsRequired.isEmpty()) && (null == keywordsOptional || keywordsOptional.isEmpty())) {
				 if (keywords.size() == 1){
					 keywordsRequired = keywords;
				 } else {
					 keywordsOptional = keywords;	 
				 }
			 }
			 
			// Adding the REQUIRED
			if (null != keywordsRequired) {
				for (String string : keywordsRequired) { // tentar adicionar direto
					keysFound.add(string);
				}
			}
			// Searching for optional
			if (null != keywordsOptional) {
				for (String string : keywordsOptional) {
					boolean foundOptional = false;
					for (Content content : contents) {
						if (content.getValue().toLowerCase().contains(string.toLowerCase())) {
							keysFound.add(string);
							foundOptional = true;
							break;
						}
					}
					// search for the optional, if dont find, search at its synonyms
					if (!foundOptional){
						for (SearchKeywordResponse keyword : searchKeywordResponseList) {
							if (string.equalsIgnoreCase(keyword.getValue()) && keyword.getSynonymList() != null) {
								for (int i = 0; i < keyword.getSynonymList().size(); i++) {
									for (Content content : contents) {
										if (content.getValue().toLowerCase().contains(keyword.getSynonymList().get(i).getValue().toLowerCase())) {
											keysFound.add(string);
											foundOptional = true;
											break;
										}
									}
									if (foundOptional){
										break;
									}
								}
							}
							if (foundOptional){
								break;
							}
						}
					}

				}
			}

		} catch (Exception ex) {
			//System.out.println("Error at getKeyFound 2:" + ex.getMessage());
			// Expected error
		}
		
		
		return keysFound;
	}

	public String getFieldValue(String fieldNameNoRef) {
		try{
			String fieldContent = "";
			boolean second = false;
			try{
				if (fieldNameNoRef.equals("RESUME_TEXT") && this.getFieldValue("TALENT_POOL_TYPE").equals("Contractor")){
					return (this.getFieldValue("snippet"));
				}
			} catch (Exception ex){
				ex.printStackTrace();
			}
			
			for(Content content : this.contents){
				if(fieldNameNoRef.equals(content.getName())){
					if (second){
						fieldContent = fieldContent + "</br>" + content.getValue();  
					}else{
						fieldContent = content.getValue(); 
						second = true;
					}
				}
			}
			return fieldContent;
		} catch (NullPointerException ex){
			return "";
		}catch (Exception ex) {
			return "ERROR";
		}
	}
	
	/**
	 * return the snippets when Practitioner == Contractor using snippet, certification and skill set fields 
	 * @param fieldNameNoRef
	 * @param keywords
	 * @return
	 */
	public String getFieldValue(String fieldNameNoRef,List <String> keywords){
		String fieldContent = "";
		try{
			if (!this.getFieldValue("TALENT_POOL_TYPE").equals("Contractor")){
				fieldContent = this.getFieldValue("RESUME_TEXT");
			} else {
				fieldContent = buildSnippet(fieldNameNoRef,keywords);
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return fieldContent;
	}
	
	public Content getFieldContent(String fieldNameNoRef) {
		for(Content content : this.contents){
			if(fieldNameNoRef.equals(content.getName())){
				return content;
			}
		}
		return null;
	}
	
	/**
	 * return the snippets when Practitioner == Contractor using snippet, certification and skill set fields 
	 * @param fieldNameNoRef
	 * @param keywords
	 * @return
	 */
	private String buildSnippet(String fieldNameNoRef,List <String> keywords){
		int snippetsSize = WexWsConstants.SNIPPETS_SIZE;
		String finalSnnipet = "";
		try{
			// 7714
			String fieldValue = this.getFieldValue("snippet").toLowerCase().replaceAll("\\<.*?>"," ").trim().replaceAll("\\s+", " ");
			String fieldValueNoLow = this.getFieldValue("snippet").replaceAll("\\<.*?>"," ").trim().replaceAll("\\s+", " ");
			//System.out.println("SNIPPET_ORIGINAL--------"+fieldValue+"--------");
//			System.out.println("KEYWORDS--------"+keywords.toString()+"--------");
			int stringLength = fieldValue.length();
			
			if (stringLength > 10){
				for (String string : keywords) {
//					System.out.println("\n------------> Searching for "+ string + " <------------");
					// 1st Index

					int index = fieldValue.indexOf(string.toLowerCase());
					if (index > -1) {
						int firstIndex = index - snippetsSize;

						if (firstIndex < 0) {
							firstIndex = 0;
						}

						int lastIndex = index + snippetsSize;

						if (lastIndex > stringLength) {
							lastIndex = stringLength;
						}

//						System.out.println(index);
//						System.out.println(firstIndex);
//						System.out.println(lastIndex);
//						System.out.println(fieldValueNoLow.substring(firstIndex, lastIndex));
//						if (finalSnnipet.length()>1){ // ???? WTF
//							finalSnnipet += " <br> ";
//						}
						String cleanSnippet = removeTruncatedWords(fieldValueNoLow.substring(firstIndex, lastIndex));
						if (cleanSnippet != "") {
							finalSnnipet += cleanSnippet;
						} else {
							finalSnnipet += fieldValueNoLow.substring(firstIndex, lastIndex);
						}
						// 2nd Index
						index = fieldValue.indexOf(string.toLowerCase(), index + 1);
						if (index > -1) {
							firstIndex = index - snippetsSize;

							if (firstIndex < 0) {
								firstIndex = 0;
							}

							lastIndex = index + snippetsSize;

							if (lastIndex > stringLength) {
								lastIndex = stringLength;
							}

//							System.out.println(index);
//							System.out.println(firstIndex);
//							System.out.println(lastIndex);
//							System.out.println(fieldValueNoLow.substring(firstIndex, lastIndex));
							
							cleanSnippet = removeTruncatedWords(fieldValueNoLow.substring(firstIndex, lastIndex));
							if (cleanSnippet != "") {
								finalSnnipet += " ||| " + cleanSnippet;
							} else {
								finalSnnipet += " ||| " + fieldValueNoLow.substring(firstIndex, lastIndex);
							}
							
						} 
//						else {
//							System.out.println("2nd occurrence not found.");
//						}

					} 
//					else {
//						System.out.println("Keyword not found.");
//					}
				}
			}
		} catch (Exception ex){
			
		}
		//System.out.println("---->" + finalSnnipet + "<----");
		return finalSnnipet;
	}

	private String removeTruncatedWords(String snippet){
		try{
			return snippet.substring(snippet.indexOf(" ") + 1,snippet.lastIndexOf(" "));
		} catch (Exception ex){
			// Impossible to remove.
			//ex.printStackTrace();
		}
		return "";
	}

	@Override
	public String toString() {
		String fieldContent = "";
		try {
			boolean second = false;
			List<Content> contentsCopy = new ArrayList<Content>(contents);
			Collections.sort(contentsCopy, new Comparator<Content>() {

				@Override
				public int compare(Content o1, Content o2) {					
					return o1.getName().compareTo(o2.getName());
				}
			});
			for (Content content : contentsCopy) {
				if (second) {
					fieldContent = fieldContent + "<br/>" + content.getName() + "=" + content.getValue();
				} else {
					fieldContent = content.getName() + "=" + content.getValue();
					second = true;
				}

			}
		} catch (NullPointerException ex){
			return "";
		}catch (Exception ex) {
			return "ERROR";
		}
		return "Document [vseKey=" + vseKey + ", score=" + score + ", contents=" + fieldContent + "]";
	}

	public List<DocumentSortKey> getSortKeys() {
		return sortKeys;
	}
	@XmlElement(name="sort-key", required=false)
	public void setSortKeys(List<DocumentSortKey> sortKeys) {
		this.sortKeys = sortKeys;
	}

	@Override
	public int compareTo(Document o) {
		if (this.score < o.score) {
			return 1;
		}

		if (this.score > o.score) {
			return -1;
		}

		return 0;
	}
	
}
