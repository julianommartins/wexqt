package com.ibm.services.tools.wexws.customfacets;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.ibm.services.tools.wexws.domain.BinningSet;
import com.ibm.services.tools.wexws.domain.Facet;
import com.ibm.services.tools.wexws.utils.XMLUtil;

public class VisaExpirationDateFacetMapper extends BaseFacetMapper {

	private static String VISA_EXP_DATE_FIELD_NAME = "VISA_EXP_DATE";
			
	private static String VISA_EXP_DATE_XPATH_TEMPLATE = 
	"dyn:map($"+VISA_EXP_DATE_FIELD_NAME+","+
		"\"viv:if-else(((substring-after(.,'|') != 'null') and (((substring-after(.,'|') - TODAY_SECONDS) > 0) and ((substring-after(.,'|') - TODAY_SECONDS) <= 15552000))),concat(substring-before(.,'|'),' - 0 - 6 months'),"+
		"viv:if-else(((substring-after(.,'|') != 'null') and (((substring-after(.,'|') - TODAY_SECONDS) > 15552000) and ((substring-after(.,'|') - TODAY_SECONDS) <= 63113904))),concat(substring-before(.,'|'),' - 6 months to 2 years'),"+
		"viv:if-else(((substring-after(.,'|') != 'null') and ((substring-after(.,'|') - TODAY_SECONDS) > 63113904)),concat(substring-before(.,'|'),' - > 2 years'),"+
		"viv:if-else((substring-after(.,'|') = 'null'),concat(substring-before(.,'|'),' - No expiration'),'dummy'))))\""+
	")";
	
	@Override
	public Facet fromNative(List<BinningSet> nativeResponses) {
		return new SimpleFacetMapperResponseGenerator(getFacetName(),nativeResponses).generate();
	}

	@Override
	public Collection<BinningSet> toNative() {
		Date today = (Date)getRequiredParameter(CustomFacetMappersConstants.PARAMS_TODAY);
		long epochInMilliseconds = today.getTime();
		int epochInSeconds = (int) (epochInMilliseconds / 1000);
		
		BinningSet binningSet = new BinningSet();
		binningSet.setId(getFacetName());
		String xpath = VISA_EXP_DATE_XPATH_TEMPLATE.replaceAll("TODAY_SECONDS", String.valueOf(epochInSeconds));
		binningSet.setSelectXPath(XMLUtil.escapeXML(xpath));
		return Arrays.asList(binningSet);
	}
}
