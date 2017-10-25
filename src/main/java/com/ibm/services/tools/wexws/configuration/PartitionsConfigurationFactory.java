package com.ibm.services.tools.wexws.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ibm.services.tools.wexws.helper.ResourceFileLoader;

public class PartitionsConfigurationFactory {
	
	private static PartitionConfiguration seatsThatIOwnInstance;	
	private static PartitionConfiguration seatsThaMatchMeInstance;

	public static PartitionConfiguration getDefaultConfigurationForSeatsThatIOwn() {
		if (seatsThatIOwnInstance == null) {
			seatsThatIOwnInstance = new PartitionsConfigurationFactory().loadFromDefaultConfigurationFileForSeatsThatIOwn();
		}
		return seatsThatIOwnInstance;
	}

	public static PartitionConfiguration getDefaultConfigurationForSeatsThatMatchMe() {
		if (seatsThaMatchMeInstance == null) {
			seatsThaMatchMeInstance = new PartitionsConfigurationFactory().loadFromDefaultConfigurationFileForSeatsThatMatchMe();
		}
		return seatsThaMatchMeInstance;
	}

	private PartitionConfiguration loadFromDefaultConfigurationFileForSeatsThatIOwn() {
		String jsonFromFile = new ResourceFileLoader("partitions_configuration_seatsthatiown.json").getContentsAsString();
		return loadFromJson(jsonFromFile);
	}
	
	private PartitionConfiguration loadFromDefaultConfigurationFileForSeatsThatMatchMe() {
		String jsonFromFile = new ResourceFileLoader("partitions_configuration_seatsthatmatchme.json").getContentsAsString();
		return loadFromJson(jsonFromFile);
	}
	
	public PartitionConfiguration loadFromJson(String json) {
		List<Partition> confList = new ArrayList<Partition>();

		try {
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(json);
			JSONArray confArray = (JSONArray) obj.get("configurations");
            Iterator<JSONObject> iterator = confArray.iterator();
            while (iterator.hasNext()) {
            	Partition qf = PartitionFactory.loadFromJsonObject(iterator.next());
				if (qf != null) {
					confList.add(qf);
				}
            }
			
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		
		return createPartitionConfiguration(confList);
	}
	
	private PartitionConfiguration createPartitionConfiguration(List<Partition> confList) {
		
		Map<String, Partition> configurationMap = new HashMap<String, Partition>();
		for (Partition partition : confList) {
			configurationMap.put(partition.getName(), partition);
		}
		return new PartitionConfiguration(configurationMap);
	}

}
