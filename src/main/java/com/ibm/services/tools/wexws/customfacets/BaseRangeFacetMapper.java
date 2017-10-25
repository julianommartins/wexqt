package com.ibm.services.tools.wexws.customfacets;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ibm.services.tools.wexws.domain.BinningSet;
import com.ibm.services.tools.wexws.domain.Facet;

public abstract class BaseRangeFacetMapper extends BaseFacetMapper{
	
	private static final String XPATH_IF_ELSE_TEMPLATE = "viv:if-else($%s %s %s and $%s %s %s,\'%s\',%s)";
	private static final String OPERATOR_GT = "&gt;";
	private static final String OPERATOR_GTE = "&gt;=";
	private static final String OPERATOR_LT = "&lt;";
	private static final String OPERATOR_LTE = "&lt;=";
	
	/**
	 * The boundaries are a ascendent sorted list of unsigned values this will
	 * be used to generate ranges Example: A list of values [0,0],(0,20],(20,40],(40,60],(60,80],(100,*] Will
	 * produce the ranges: >=0 and <=0, >0 and <=20, >20 and <=40, >40 and <=60%, >60 and <=80%, >80 and <=100%, >100%
	 */
	protected List<String> rangeBoundaries;
	protected Map<String,String> labels;
	protected String fieldName;
	
	public List<String> getRangeBoundaries() {
		return rangeBoundaries;
	}
	public void setRangeBoundaries(List<String> rangeBoundaries) {
		this.rangeBoundaries = rangeBoundaries;
		this.labels = getLabels();
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	
	@Override
	public Collection<BinningSet> toNative() {
		String xpathExpression = generateIfExpressionRec(rangeBoundaries.iterator()).toString();
		BinningSet facetRequest = new BinningSet();
		facetRequest.setId(facetName);
		facetRequest.setSelectXPath(xpathExpression);
		return Arrays.asList(facetRequest);
	}
	
	@Override
	public Facet fromNative(List<BinningSet> nativeResponses) {
		return new SimpleFacetMapperResponseGenerator(getFacetName(), nativeResponses,
				new LabelsComparator(labels.values())).generate();
	}
	
	protected StringBuilder generateIfExpressionRec(Iterator<String> iterator) {
		if (iterator.hasNext()) {
			String range = iterator.next();
			
			String d1 = getFirstDelimiter(range);
			long v1 = getFirstValue(range);
			long v2 = getSecondValue(range);
			String d2 = getSecondDelimiter(range);
			String label = labels.get(range);
			String operator1 = null;
			String operator2 = null;
			
			StringBuilder xpathIfElse = new StringBuilder();

			if("[".equals(d1)  && "]".equals(d2) ){
				operator1 = OPERATOR_GTE;
				operator2 = OPERATOR_LTE;
			}else
			if("(".equals(d1)  && "]".equals(d2) ){
				operator1 = OPERATOR_GT;
				operator2 = OPERATOR_LTE;
			}else
			if("[".equals(d1)  && ")".equals(d2) ){
				operator1 = OPERATOR_GTE;
				operator2 = OPERATOR_LT;
			}else
			if("(".equals(d1)  && ")".equals(d2) ){
				operator1 = OPERATOR_GT;
				operator2 = OPERATOR_LT;
			}
			return xpathIfElse.append(String.format(XPATH_IF_ELSE_TEMPLATE, fieldName, operator1, v1, fieldName, operator2, v2, label, generateIfExpressionRec(iterator).toString()));
		} else {
			return new StringBuilder("'"+labels.get("OTHERS")+"'");
		}
	}
	
	private String getFirstDelimiter(String range) {
		return range.substring(0,1);
	}

	private long getFirstValue(String range) {
		int idx = range.indexOf(",");
		String value = range.substring(1,idx).trim();
		return Long.parseLong(value);
	}

	private long getSecondValue(String range) {
		long lvalue=0;
		int idx1 = range.indexOf(",");
		int idx2 = range.indexOf("]",idx1);
		if(idx2==-1){
			idx2 = range.indexOf(")",idx1);
		}
		if(idx1>-1 && idx1<idx2){
			String value = range.substring(idx1+1,idx2).trim();
			if("*".equals(value)) return Long.MAX_VALUE;
			lvalue = Long.parseLong(value);
		}
		
		return lvalue;
	}

	private String getSecondDelimiter(String range) {
		return range.substring(range.length()-1);
	}
	
	protected String generateLabel(String range) {
		String c1 = getFirstDelimiter(range);
		long v1 = getFirstValue(range);
		long v2 = getSecondValue(range);
		String label="?";
		if(v1==v2 || v2 == Long.MAX_VALUE){
			label = String.valueOf(v1).concat("%");
		}else{
		    label = String.valueOf(v1).concat(" - ").concat(String.valueOf(v2)).concat("%");
		}
		if("(".equals(c1)){
			label=">"+label;
		}
		return label;
	}
	
	private Map<String,String> getLabels() {
		Map<String,String> result = new LinkedHashMap<String,String>(rangeBoundaries.size());
		for (String range : rangeBoundaries) {
			result.put(range,generateLabel(range));
		}
		result.put("OTHERS",getOtherLabel());
		
		return result;
	}
	
	protected abstract String getOtherLabel();
}
