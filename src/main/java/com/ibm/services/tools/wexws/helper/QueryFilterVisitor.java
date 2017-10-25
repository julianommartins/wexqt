package com.ibm.services.tools.wexws.helper;

import com.ibm.services.tools.wexws.domain.AllTextFieldsFilter;
import com.ibm.services.tools.wexws.domain.AndFilter;
import com.ibm.services.tools.wexws.domain.OrFilter;
import com.ibm.services.tools.wexws.domain.RangeFilter;
import com.ibm.services.tools.wexws.domain.RecordFilter;
import com.ibm.services.tools.wexws.domain.TextFilter;
import com.ibm.services.tools.wexws.domain.XPathFilter;

public interface QueryFilterVisitor {

	public void visit(AllTextFieldsFilter allTextFilter);
	public void visit(TextFilter textFilter);
	public void visit(RangeFilter rangeFilter);
	public void visit(RecordFilter recordFilter);
	public void visit(XPathFilter xpathFilter);
	public void visit(OrFilter orFilter);
	public void visit(AndFilter andFilter);
	public void preVisit(OrFilter orFilter);
	public void preVisit(AndFilter andFilter);
}
