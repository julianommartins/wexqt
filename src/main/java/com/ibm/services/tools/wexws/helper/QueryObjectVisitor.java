package com.ibm.services.tools.wexws.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.ibm.services.tools.wexws.dao.AccessLayerDAO;
import com.ibm.services.tools.wexws.domain.AllTextFieldsFilter;
import com.ibm.services.tools.wexws.domain.AndFilter;
import com.ibm.services.tools.wexws.domain.Filter;
import com.ibm.services.tools.wexws.domain.OrFilter;
import com.ibm.services.tools.wexws.domain.RangeFilter;
import com.ibm.services.tools.wexws.domain.RecordFilter;
import com.ibm.services.tools.wexws.domain.TextFilter;
import com.ibm.services.tools.wexws.domain.XPathFilter;
import com.ibm.services.tools.wexws.utils.XMLUtil;

/**
 * This class is responsible to build the XML content to be passed in the query-object rest api param.
 * @author Daniel Paganotti
 */
public class QueryObjectVisitor implements QueryFilterVisitor {

	private static final Logger logger = Logger.getLogger(QueryObjectVisitor.class);
	
	private static final Map<String, Pattern> replacementPatternMap = createReplacementPatternMap();
	private static JAXBContext operatorJaxbContext;
	
	private AccessLayerDAO wexDAO;
	private Stack<FilterContext> contexts = new Stack<FilterContext>();
	
	public QueryObjectVisitor(AccessLayerDAO wexDAO) {
		this.wexDAO = wexDAO;
		if (operatorJaxbContext == null) {
			try {
				operatorJaxbContext = JAXBContext.newInstance(Operator.class);
			} catch (JAXBException e) {
				String message = "Error while creating JAXBContext for Operator.class";
				logger.error(message, e);
				throw new RuntimeException(message, e);
			}
		}
		pushContext();
	}
	
	@Override
	public void visit(AllTextFieldsFilter allTextFilter) {
		currentContext().filters.add(allTextFilter.getSearchCriteria());
	}

	@Override
	public void visit(TextFilter textFilter) {
		String searchString = createSearchByFieldString(textFilter.getFieldName(), textFilter.getTextFilter());
		currentContext().filters.add(searchString);

	}

	@Override
	public void visit(RangeFilter rangeFilter) {
		currentContext().filters.add(getXPathForRangeFilter(rangeFilter));
	}

	@Override
	public void visit(RecordFilter recordFilter) {
		currentContext().filters.add(getXPathForRecordFilter(recordFilter));
	}

	@Override
	public void visit(XPathFilter xpathFilter) {
		currentContext().filters.add(xpathFilter);
	}
	
	@Override
	public void preVisit(OrFilter orFilter) {
		pushContext();
	}
	
	@Override
	public void visit(OrFilter orFilter) {
		Object filterToBeAdded = null;
		final boolean hasChildrenRequests = hasChildrenFilters();
		if (hasChildrenRequests) {
			filterToBeAdded = getFilterToBeAdded("or");
		}
		popContext();
		if (hasChildrenRequests) {
			currentContext().filters.add(filterToBeAdded);
		}
	}

	@Override
	public void preVisit(AndFilter andFilter) {
		pushContext();
	}
	
	@Override
	public void visit(AndFilter andFilter) {
		Object filterToBeAdded = null;
		final boolean hasChildrenRequests = hasChildrenFilters();
		if (hasChildrenRequests) {
			filterToBeAdded = getFilterToBeAdded("and");
		}
		popContext();
		if (hasChildrenRequests) {
			currentContext().filters.add(filterToBeAdded);
		}
	}

	private Object getFilterToBeAdded(String logic) {
		Object filterToBeAdded;
		Object firstObject = currentContext().filters.get(0);
		if (firstObject instanceof XPathFilter) {
			StringBuilder xpathBuilder = new StringBuilder();
			for (Object xpath: currentContext().filters) {
				if (xpathBuilder.length() > 0) {
					xpathBuilder.append(" "+logic+" ");
				}
				xpathBuilder.append(xpath);
			}
			filterToBeAdded = WexRequestBuilders.xpathFilter("(" + xpathBuilder.toString() + ")");
		}else if (firstObject instanceof String) {
			StringBuilder query = new StringBuilder();
			for (Object textFilter: currentContext().filters) {
				if (query.length() > 0) {
					query.append(" "+logic+" ");
				}
				query.append(textFilter);
			}
			filterToBeAdded = "(" + query.toString() + ")";
		}else {
			Operator childOperator = new Operator();
			childOperator.setLogic(logic);
			childOperator.setTermList(new ArrayList<Term>());
			for (Object termElement: currentContext().filters) {
				childOperator.getTermList().add((Term)termElement);
			}
			filterToBeAdded = childOperator;
		}
		return filterToBeAdded;
	}
	
	private Filter getXPathForRecordFilter(RecordFilter recordFilter) {
		return WexRequestBuilders.xpathFilter("$"+recordFilter.getFieldName()+"='"+XMLUtil.escapeXML(recordFilter.getValue())+"'");
	}
	
	private Filter getXPathForRangeFilter(RangeFilter rangeFilter) {
		StringBuilder conditionXpath = new StringBuilder();
		conditionXpath.append("$"+rangeFilter.getFieldName());
		
		switch (rangeFilter.getOperator()) {
		case LTEQ:
			conditionXpath.append(" <= ").append(rangeFilter.getFirstValue());
			break;
		case GTEQ:
			conditionXpath.append(" >= ").append(rangeFilter.getFirstValue());
			break;
		case WITHIN_RANGE:
			conditionXpath.append(" >= ").append(rangeFilter.getFirstValue())
			.append(" and ").append("$"+rangeFilter.getFieldName()).append(" <= ")
			.append(rangeFilter.getSecondValue());
			break;			
		}
		return WexRequestBuilders.xpathFilter(conditionXpath.toString());
	}
	
	private static Term buildQueryTermElement(String field, String value) {
		Term term = new Term();
		term.setField(field);
		term.setValue(value);
		return term;
	}
	
	private void pushContext() {
		contexts.push(new FilterContext());
	}
	
	private void popContext() {
		contexts.pop();
	}
	
	private FilterContext currentContext() {
		return contexts.peek();
	}
	
	private boolean hasChildrenFilters() {
		return currentContext().filters != null
				&& currentContext().filters.size() > 0;
	}
	
	private boolean hasFilters() {
		return !currentContext().filters.isEmpty();
	}
	
	private static class FilterContext {
		public final List<Object> filters = new ArrayList<Object>();
	}
	
	@XmlRootElement(name="operator")
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Operator {
		
		@XmlAttribute(name="precedence", required=false)
		private String precedence;
		
		@XmlAttribute(name="logic", required=false)
		private String logic;
		
		@XmlAttribute(name="char", required=false)
		private String charAttribute;
		
		@XmlElement(name="term", required=false)
		private List<Term> termList;
		
		@XmlElement(name="operator", required=false)
		private List<Operator> nestedOperators;
		
		public void setLogic(String logic) {
			this.logic = logic;
		}
		
		@SuppressWarnings("unused")
		public void setCharAttribute(String charAttribute) {
			this.charAttribute = charAttribute;
		}
		
		public List<Term> getTermList() {
			return termList;
		}
		public void setTermList(List<Term> termList) {
			this.termList = termList;
		}
		
		public void addNestedOperator(Operator operator) {
			if (nestedOperators == null) {
				nestedOperators = new ArrayList<Operator>();
			}
			nestedOperators.add(operator);
		}
	}
	
	@SuppressWarnings("unused")
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Term {
		@XmlAttribute(name="field")
		private String field;
		@XmlAttribute(name="str")
		private String value;
		@XmlAttribute(name="weight", required=false)
		private String weight;
		@XmlElement(name="operator", required=false)
		private Operator operator;
				
		public void setField(String field) {
			this.field = field;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public void setWeight(String weight) {
			this.weight = weight;
		}
	}
	
	public String generateQueryObjectXML() throws RuntimeException {
		String xml = null;
		if (hasFilters()) {
			StringWriter stringWriter = new StringWriter();
			try {
				Operator queryRootOperator = buildRootOperator();
				Marshaller m = operatorJaxbContext.createMarshaller();
		        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		        	        
		        m.marshal(queryRootOperator, stringWriter);
		        xml = stringWriter.toString();
		        xml = xml.substring(xml.indexOf("<operator"), xml.length()); //removing <?xml?> tag
			}catch(Exception e) {
				logger.error("Error while marshalling Operator.class = "+e.getMessage(), e);
				throw new RuntimeException(e);
			}finally {
				try {
					stringWriter.close();
				} catch (IOException e) {
					logger.error("IOException while closing stringWriter = "+e.getMessage(), e);
				}
			}
		}
		return xml;
	}

	private Operator buildRootOperator() throws Exception{
		Operator queryRootOperator = new Operator();
		queryRootOperator.setLogic("and");
		queryRootOperator.setTermList(new ArrayList<Term>());
		
		StringBuilder conditionXPath = new StringBuilder();
		StringBuilder querySearch = new StringBuilder();
		for (Object filter: currentContext().filters) {
			if (filter instanceof XPathFilter) {
				if (conditionXPath.length() > 0) {
					conditionXPath.append(" and ");
				}
				conditionXPath.append("("+filter+")");
			}else if (filter instanceof String) {
				if (querySearch.length() > 0) {
					querySearch.append(" and ");
				}
				querySearch.append("(").append(filter).append(")");
			}else if (filter instanceof Operator) {
				queryRootOperator.addNestedOperator((Operator)filter);
			}else {
				queryRootOperator.getTermList().add((Term)filter);
			}
		}
		
		if (conditionXPath.length() > 0) {
			Term conditionXPathTerm = buildQueryTermElement("v.condition-xpath", conditionXPath.toString());
			queryRootOperator.getTermList().add(conditionXPathTerm);
		}
		
		if (querySearch.length() > 0) {
			Operator querySearchOperator = parseQueryString(querySearch);
			queryRootOperator.addNestedOperator(querySearchOperator);			
		}
		return queryRootOperator;
	}

	private Operator parseQueryString(StringBuilder querySearch) throws JAXBException, IOException {
		String parserResponseXML = wexDAO.executeQueryParser(adjustOperatorsCase(querySearch.toString()));
		Unmarshaller m = operatorJaxbContext.createUnmarshaller();
		InputStream stream = IOUtils.toInputStream(parserResponseXML); 
		Operator querySearchOperator = (Operator)m.unmarshal(stream);
		stream.close();
		return querySearchOperator;
	}
	
	private static Map<String, Pattern> createReplacementPatternMap() {
		HashMap<String, Pattern> result = new HashMap<String, Pattern>();
		result.put("OR", Pattern.compile("(?<=\\s)or(?=\\s)"));
		result.put("NOT", Pattern.compile("(?<=\\s)not(?=\\s)"));
		result.put("and", Pattern.compile("(?<=\\s)AND(?=\\s)"));
		return result;
	}
	
	private static String adjustOperatorsCase(String text) {
		String result = text;
		for (String replacement : replacementPatternMap.keySet()) {
			result = replaceMatchesWithReplacement(result, replacement);
		}
		return result;
	}

	private static String replaceMatchesWithReplacement(String result,
			String replacement) {
		return replacementPatternMap.get(replacement).matcher(result)
				.replaceAll(replacement);
	}

	private String createSearchByFieldString(String fieldName, String textSearch) {
		return String.format("%s:(%s)", fieldName, textSearch);
	}
}