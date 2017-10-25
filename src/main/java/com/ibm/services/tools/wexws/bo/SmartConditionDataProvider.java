package com.ibm.services.tools.wexws.bo;

import com.ibm.services.tools.wexws.domain.Location;
import com.ibm.services.tools.wexws.domain.ServiceServiceArea;

public interface SmartConditionDataProvider {
	Iterable<Location> getFullLocations();
	Iterable<Location> getCountryLocations();
	Iterable<ServiceServiceArea> getServiceServiceAreas();
}
