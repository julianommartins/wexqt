package com.ibm.services.tools.wexws.configuration;

import java.util.HashMap;
import java.util.Map;

public class PartitionConfiguration {
	
	private Map<String, Partition> configurationMap;

	PartitionConfiguration(Map<String, Partition> configurationMap) {
		super();
		this.configurationMap = configurationMap;
	}

	public PartitionConfiguration() {

		this.configurationMap = new HashMap<String, Partition>();
	}

	public Map<String, Partition> getPartitionConfigurationMap() {
		return configurationMap;
	}
	
	public Partition getPartitionConfiguration(String partitionName) {
		return configurationMap.get(partitionName);
	}

}
