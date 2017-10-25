package com.ibm.services.tools.wexws.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JapaneseQueryExtractor {
	
	private static String JAPANESE_CHARS_REGEX = "[\\p{InHiragana}\\p{InKatakana}]*+";
	
	/**
	 * To validade if query is JP or not
	 * @param query
	 * @return
	 */
	public boolean isJapaneseQuery(String query) {
		Pattern pat = Pattern.compile("/[\u3000-\u303F]|[\u3040-\u309F]|[\u30A0-\u30FF]|[\uFF00-\uFFEF]|[\u4E00-\u9FAF]|[\u2605-\u2606]|[\u2190-\u2195]|\u203B/g");
		try{
			Matcher m = pat.matcher(query);
			return m.find();	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public String getJapaneseQuery(String query) {
		
		Pattern pat = Pattern.compile(JAPANESE_CHARS_REGEX);
		StringBuilder sb = new StringBuilder();
		try{
			Matcher m = pat.matcher(query);
			while (m.find()) {
				sb.append(m.group() + " ");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sb.toString().replaceAll("\\s+", " ").trim();
	}
	
	public String getNonJapaneseQuery(String query) {
		try{
			return query.replaceAll(JAPANESE_CHARS_REGEX, "").replaceAll("\\s+", " ").trim();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return query;
	}

}
