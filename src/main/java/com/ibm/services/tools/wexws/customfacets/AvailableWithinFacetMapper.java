package com.ibm.services.tools.wexws.customfacets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.ibm.services.tools.wexws.domain.Bin;
import com.ibm.services.tools.wexws.domain.BinningSet;
import com.ibm.services.tools.wexws.domain.Facet;
import com.ibm.services.tools.wexws.domain.FacetValue;
import com.ibm.services.tools.wexws.utils.XMLUtil;

public class AvailableWithinFacetMapper extends BaseFacetMapper {

	protected String fieldName;
	
	@Override
	public Facet fromNative(List<BinningSet> nativeResponses) {
		Collection<BinningSet> myFacets = findMyFacets(nativeResponses);
		if (myFacets == null || myFacets.size() == 0) {
			return null;
		}
		nativeResponses.removeAll(myFacets);
		return convertNativeFacets(myFacets);
	}

	@Override
	public Collection<BinningSet> toNative() {
		Date today = (Date)getRequiredParameter(CustomFacetMappersConstants.PARAMS_TODAY);
		long epochInMilliseconds = today.getTime();
		int epochInSeconds = (int) (epochInMilliseconds / 1000);
		
		return buildFacetRequests(epochInSeconds);
	}

	@Override
	public Collection<Object> generateSelection(Collection<String> selections) {
		Collection<Object> nativeSelectionList = new ArrayList<Object>(2);
		for (String selection: selections){
			for (AvailableWithinFacetBucket bucket: AvailableWithinFacetBucket.values()){
				if (bucket.getLabel().equals(selection)){
					nativeSelectionList.add(bucket.getNativeFacetName(getFacetName()) + "==" + selection);
					break;
				}
			}
		}
		return nativeSelectionList;
	}
	
	private Facet convertNativeFacets(Collection<BinningSet> myFacets) {
		List<FacetValue> facetValues = new ArrayList<FacetValue>();
		
		for (AvailableWithinFacetBucket bucket: AvailableWithinFacetBucket.values()) {
			String facetId = bucket.getNativeFacetName(getFacetName());
			for (BinningSet binningSet: myFacets){
				if (facetId.equals(binningSet.getId()) && binningSet.getBins() != null) {
					for (Bin bin: binningSet.getBins()){
						if (!CustomFacetMappersConstants.DUMMY_FACET_VALUE.equals(bin.getLabel())){
							FacetValue facetValue = new FacetValue(bucket.getLabel(), bin.getNdocs(), bucket.getLabel());
							facetValues.add(facetValue);
						}
					}
				}
				
			}
		}
		
		Facet facetResponse = new Facet(getFacetName());
		facetResponse.setValues(facetValues);
		return facetResponse;
	}
	
	private Collection<BinningSet> findMyFacets(List<BinningSet> nativeResponses) {
		Collection<BinningSet> availableWithinFacets = new ArrayList<BinningSet>(6);
		for (BinningSet binningSet: nativeResponses){
			if (isOneOfTheAvailableWithinNativeFacets(binningSet)){
				availableWithinFacets.add(binningSet);
			}
		}
		return availableWithinFacets;
	}
	
	private boolean isOneOfTheAvailableWithinNativeFacets(final BinningSet facet) {
		boolean found = false;
		for (AvailableWithinFacetBucket bucket: AvailableWithinFacetBucket.values()){
			if (facet.getId().equals(bucket.getNativeFacetName(getFacetName()))){
				found = true;
				break;
			}
		}
		return found;
	}
	
	private Collection<BinningSet> buildFacetRequests(final int epochInSeconds) {
		List<BinningSet> facetRequests = new ArrayList<BinningSet>(6);
		for (AvailableWithinFacetBucket bucket: AvailableWithinFacetBucket.values()){
			BinningSet facetRequest = new BinningSet();
			facetRequest.setId(bucket.getNativeFacetName(getFacetName()));
			facetRequest.setSelectXPath(generateXPathForFacet(epochInSeconds, bucket));
			facetRequests.add(facetRequest);
		}
		return facetRequests;
	}
	
	private String generateXPathForFacet(int epochInSeconds, AvailableWithinFacetBucket bucketInfo) {
		String xPath;
		switch (bucketInfo) {
		case MORE_THAN_90_DAYS:
			xPath = generateCatchAllFacetXPath(epochInSeconds, bucketInfo);
			break;
		case NO_AVAIL_DATE:
			xPath = generateXPathForNoAvailDate();
			break;
		default:
			xPath = generateLessThanXPath(epochInSeconds, bucketInfo);

		}
		return xPath;
	}
	
	private String generateXPathForNoAvailDate() {
		return String
				.format("viv:if-else(boolean($%s) = false(),\'%s\',%s)",
						fieldName,
						AvailableWithinFacetBucket.NO_AVAIL_DATE.getLabel(),
						CustomFacetMappersConstants.AVAILABLE_WITHIN_DUMMY_VALUE_XPATH);
	}

	private String generateCatchAllFacetXPath(int epochInSeconds,
			AvailableWithinFacetBucket bucketInfo) {
		return String
				.format("viv:if-else($%s - %d &gt;= %d,\'%s\',%s)",
						fieldName,
						epochInSeconds,
						bucketInfo.getBucketInSeconds(),
						XMLUtil.escapeXML(bucketInfo.getLabel()),
						CustomFacetMappersConstants.AVAILABLE_WITHIN_DUMMY_VALUE_XPATH);
	}

	private String generateLessThanXPath(int epochInSeconds,
			AvailableWithinFacetBucket bucketInfo) {
		return String
				.format("viv:if-else($%s - %d &lt; %d,\'%s\',%s)",
						fieldName,
						epochInSeconds,
						bucketInfo.getBucketInSeconds(),
						XMLUtil.escapeXML(bucketInfo.getLabel()),
						CustomFacetMappersConstants.AVAILABLE_WITHIN_DUMMY_VALUE_XPATH);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
