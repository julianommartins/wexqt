package com.ibm.services.tools.wexws.domain;

import com.ibm.services.tools.wexws.helper.QueryFilterVisitor;

public interface Filter {

	void accept(QueryFilterVisitor visitor);
}
