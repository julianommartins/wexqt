package com.ibm.services.tools.wexws.customfacets;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.ibm.services.tools.wexws.domain.BinningSet;
import com.ibm.services.tools.wexws.domain.Facet;

public class SingleFieldHierarchyFacetMappper extends BaseFacetMapper {

	private String separator;
	private String hierarchyBindingFieldName;
	private List<String> fieldsHierarchy;
	
	@Override
	public Facet fromNative(List<BinningSet> nativeResponses) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<BinningSet> toNative() {
		/**<binning-set bs-id="CERTIFICATION" logic="or" select="$CERTIFICATION_FACET" disable-bin-pruning="disable-bin-pruning" max-bins="-1">
		<binning-tree delimiter=">"/>
		</binning-set>**/ 
		
		Boolean retrieveOnlyRootLevel = (Boolean)arguments.get(CustomFacetMappersConstants.RETRIEVE_ONLY_ROOT_HIERARCHICAL_LEVEL_PARAM);
		if (retrieveOnlyRootLevel != null && retrieveOnlyRootLevel.booleanValue()){
			String xpath = "dyn:map($"+hierarchyBindingFieldName+", \"substring-before(.,'"+separator+"')\")";
			
		}else{
			
		}
		return null;
	}

	@Override
	public Collection<Object> generateSelection(Collection<String> selections) {
		// TODO Auto-generated method stub
		return null;
	}

}
