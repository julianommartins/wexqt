package com.ibm.services.tools.wexws.customfacets;

public class LastWeekUtilizationMapper extends BaseRangeFacetMapper {

	@Override
	protected String getOtherLabel() {
		return "No Last Week Utilization";
	}

}
