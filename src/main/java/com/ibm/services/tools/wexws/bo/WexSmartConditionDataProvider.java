package com.ibm.services.tools.wexws.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.log4j.Logger;

import com.ibm.services.tools.wexws.dao.WexRestfulDAO;
import com.ibm.services.tools.wexws.domain.Facet;
import com.ibm.services.tools.wexws.domain.FacetRequest;
import com.ibm.services.tools.wexws.domain.FacetValue;
import com.ibm.services.tools.wexws.domain.Location;
import com.ibm.services.tools.wexws.domain.Request;
import com.ibm.services.tools.wexws.domain.Response;
import com.ibm.services.tools.wexws.domain.ServiceServiceArea;
import com.ibm.services.tools.wexws.helper.WexRequestBuilders;

public class WexSmartConditionDataProvider implements SmartConditionDataProvider {

	private static final Logger logger = Logger.getLogger(WexSmartConditionDataProvider.class);
	
	private static final String HIERARCHY_FIELDS_SEPARATOR = "_sep_";
	private static final String[] LOCATION_FIELDS = new String[]{"WORK_LOCATION_IOT", "WORK_LOCATION_IMT", "WORK_LOCATION_COUNTRY", "WORK_LOCATION_STATE", "WORK_LOCATION_CITY"};
	//private static final String[] SERVICE_SERVICE_AREA_FIELDS = new String[]{"SERVICE", "SERVICE_AREA"};
	
	private final WexRestfulDAO wexDAO;
	private final Collection<Location> fullLocations;
	private final Collection<Location> countryLocations;
	private final Collection<ServiceServiceArea> serviceServiceAreas;
	
	public WexSmartConditionDataProvider(WexRestfulDAO wexDAO) {
		super();
		this.wexDAO = wexDAO;
		serviceServiceAreas = new ArrayList<ServiceServiceArea>();
		fullLocations = new ArrayList<Location>(100);
		countryLocations = new HashSet<Location>();
		populateData();
	}
	
	@Override
	public Iterable<Location> getFullLocations() {
		return fullLocations;
	}
	
	@Override
	public Iterable<Location> getCountryLocations() {
		return countryLocations;
	}
	
	@Override
	public Iterable<ServiceServiceArea> getServiceServiceAreas() {
		return serviceServiceAreas;
	}
	
	private void populateData(){
		Response response = executeFacetRequests();
		populateLocations(response);
		//populateServiceServiceArea(response);
	}
	
//	private void populateServiceServiceArea(Response response){
//		Facet facet = response.getFacetByName("SERVICE_SERVICE_AREA");
//		if (facet != null){
//			for (FacetValue value: facet.getValues()){
//				String[] values = value.getLabel().split(HIERARCHY_FIELDS_SEPARATOR);
//				if (values.length > 1){
//					ServiceServiceArea serviceServiceArea = new ServiceServiceArea(values[0], values[1]);
//					serviceServiceAreas.add(serviceServiceArea);
//				}
//			}
//		}
//	}
	
	private void populateLocations(Response response){
		Facet locationFacet = response.getFacetByName("LOCATION");
		if (locationFacet != null){
			for (FacetValue value: locationFacet.getValues()){
				String[] locationValues = value.getLabel().split(HIERARCHY_FIELDS_SEPARATOR);
				if (locationValues.length > 2){
					Location fullLocation = new Location();
					fullLocation.setIOT(locationValues[0]);
					fullLocation.setIMT(locationValues[1]);
					fullLocation.setCountry(locationValues[2]);
					fullLocation.setState(locationValues[3]);
					if (locationValues.length == 5){
						fullLocation.setCity(locationValues[4]);
					}
					fullLocations.add(fullLocation);
					
					Location countryLocation = new Location();
					countryLocation.setIOT(locationValues[0]);
					countryLocation.setIMT(locationValues[1]);
					countryLocation.setCountry(locationValues[2]);
					countryLocations.add(countryLocation);
				}
			}
		}
	}
	
	private Response executeFacetRequests(){
		Response response = null;
		Request request = WexRequestBuilders.searchRequest("PMP_Practitioner");
		
		request.addFacetRequest(buildHierarchyFacetRequest("LOCATION", LOCATION_FIELDS));
//		request.addFacetRequest(buildHierarchyFacetRequest("SERVICE_SERVICE_AREA", SERVICE_SERVICE_AREA_FIELDS));
		
		try {
			response = wexDAO.executeQuery(request);
		} catch (Exception e) {
			logger.error("Exception in executeFacetRequests(), msg="+e.getMessage());
		}
		return response;
	}
	
	private FacetRequest buildHierarchyFacetRequest(String facetName, String... fields){
		FacetRequest facetRequest = WexRequestBuilders.facetRequest(facetName)
				.withSelectXPath(buildHierarchyFacetXPath(fields)).withMaximumNumberOfValues(Integer.MAX_VALUE);
		return facetRequest;
	}
	
	private String buildHierarchyFacetXPath(String... fields){
		StringBuilder builder = new StringBuilder();
		builder.append("concat(");
		for (int i=0; i < fields.length; i++){
			if (i > 0){
				builder.append(",'").append(HIERARCHY_FIELDS_SEPARATOR).append("',");
			}
			builder.append("$").append(fields[i]);
		}
		builder.append(")");
		return builder.toString();
		
	}
}
