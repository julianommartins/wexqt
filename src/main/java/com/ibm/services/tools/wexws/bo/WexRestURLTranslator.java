package com.ibm.services.tools.wexws.bo;

import java.util.List;

import com.ibm.services.tools.wexws.domain.BinningSet;
import com.ibm.services.tools.wexws.domain.SortBy;

/**
 * A URL translator is responsible to translate some kind of request to the WEX REST api parameters
 * @author Daniel Paganotti
 *
 */
public interface WexRestURLTranslator {

	public List<String> getRequestedFields();
	public List<BinningSet> getRequestedFacets();
	public List<String> getFacetSelections();
	public String getTextSearch();
	public String getQueryObject();
	public int getStartAt();
	public int getMax();
	public List<SortBy> getSortList();
	public boolean getProfile();
	public boolean getNlq();
	public boolean getStemming();
	public boolean getSpelling();
	public String getOntolectionName();
	public boolean getSmartConditition();
	public String getSmartConditionOperator();
	public String getAuthorizationRights();
	public boolean getJapaneseSearch();
	public boolean getBold();
	public boolean getBrowse();
	public String getBoldRootClassName();
	public List<String> getFieldsToBold();
}
