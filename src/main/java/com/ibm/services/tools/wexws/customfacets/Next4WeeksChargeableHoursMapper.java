package com.ibm.services.tools.wexws.customfacets;

public class Next4WeeksChargeableHoursMapper extends BaseRangeFacetMapper {

	@Override
	protected String getOtherLabel() {
		return generateLabel(rangeBoundaries.get(0));
	}

}
