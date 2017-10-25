package com.ibm.services.tools.wexws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.ibm.services.tools.wexws.collections.Collection;
import com.ibm.services.tools.wexws.collections.Server;
import com.ibm.services.tools.wexws.collections.Collection.CollectionBuilder;
import com.ibm.services.tools.wexws.collections.CollectionShard;
import com.ibm.services.tools.wexws.factory.WexRestfulFactory;

public class WexConfiguration {
	
	private String environmentId;
	private Properties prop;
	//private Map<String, List<String>> shardsByCollectionName;
	private Map<String,Collection> collectionMap;
	private Map<String,CollectionShard> collectionShardMap;
	private Map<String,String> ontolectionMap;
	//private List<String> servers;
	private List<Server> servers;
	
	public WexConfiguration(String environmentId) {
		this.environmentId = environmentId;
		this.prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("wexdaofactory."+environmentId+".properties"));
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public List<String> getServes(){
//		return servers;
//	}
	
	public List<Server> getServers(){
		return servers;
	}

	public int getPort(){
		return Integer.parseInt(get("wex.port"));
	}
	
	public String getUser(){
		return get("wex.user");
	}
	
	public String getPassword(){
		return get("wex.password");
	}
	
	public int getHTTPConnectTimeout(){
		return getInt("http.connect.timeout");
	}
	public int getWexQueryTimeout(){
		return getInt("wex.query.timeout");
	}
	public String getArena(String collectionName){
		return get(collectionName+".arena");
	}

//	public List<String> getShards(String collectionName){
//		return shardsByCollectionName.get(collectionName);
//	}
	public List<CollectionShard> getShards(String collectionName){
		return collectionMap.get(collectionName).getShards();
	}

//	private List<String> lookupServers(){
//		List<String> servers = new ArrayList<String>(5);
//		for(int i=1; i<100; i++){
//			String server = get("wex.server."+i);
//			if(server==null) break;
//			servers.add(server);
//		}
//		return servers;
//	}
//	

	private List<Server> lookupServers(){
		List<Server> servers = new ArrayList<Server>();
		for(int i=1; i<100; i++){
			String serverAddress = get("wex.server."+i);
			if(serverAddress==null) break;
			Server server = new Server(i, serverAddress);
			servers.add(server);
		}
		return servers;
	}
	

	private void init(){
		collectionMap = new HashMap<String, Collection>();
		collectionShardMap = new HashMap<String, CollectionShard>();
		ontolectionMap = new HashMap<String, String>();
		servers = lookupServers();
		
		for(Entry<Object,Object> entry : prop.entrySet()){
			String key = (String)entry.getKey();
			if(key.endsWith(".shards")){
				String collectionName = key.substring(0, key.indexOf(".shards"));

				CollectionBuilder collectionBuilder = Collection.getBuilder(collectionName);

				for( String shardString : ((String)entry.getValue()).split(",")) {
					int idx = shardString.indexOf(":");
					String shardName = shardString.substring(0,idx);
					
					for(String serversIndex : shardString.substring(idx+1).split(":")){
						int i = Integer.parseInt(serversIndex);
						collectionBuilder.withShardOnServer(shardName, getServerById(i));
					}
					
					Collection collection = collectionBuilder.build();
					collectionMap.put(collection.getCollectionName(), collection);
					
					for (CollectionShard shard : collection.getShards()) {
						collectionShardMap.put(shard.getShardName(), shard);
					}
				}	
			}
			
			if (key.endsWith(".ontolection")){
				String collectionName = key.substring(0, key.indexOf(".ontolection"));
				ontolectionMap.put(collectionName, (String)entry.getValue());
				
			}
		}
	}
	
	private Server getServerById(int id) {
		for (Server server : servers) {
			if (server.getId() == id)
				return server;
		}
		return null;
	}	
	
	public Server getServerByAddress(String address) {
		for (Server server : servers) {
			if (server.getAddress().equals(address))
				return server;
		}
		return null;
	}

//	private void init(){
//		shardsByCollectionName = new HashMap<String, List<String>>();
//		collectionMap = new HashMap<String, Collection>();
//		ontolectionMap = new HashMap<String, String>();
//		servers = lookupServers();
//		
//		for(Entry<Object,Object> entry : prop.entrySet()){
//			String key = (String)entry.getKey();
//			if(key.endsWith(".shards")){
//				String collectionName = key.substring(0, key.indexOf(".shards"));
//				List<String> shards = new ArrayList<String>(4);
//				shardsByCollectionName.put(collectionName, shards);
//				for( String shard : ((String)entry.getValue()).split(",")) {
//					int idx = shard.indexOf(":");
//					String name = shard.substring(0,idx);
//					shards.add(name);
//					
//					List<Integer> serverList = new ArrayList<Integer>(4);
//					for(String serversIndex : shard.substring(idx+1).split(":")){
//						int i = Integer.parseInt(serversIndex);
//						serverList.add(i);
//					}
//					Collection c = new Collection(name, serverList);
//					collectionMap.put(c.getCollectionName(), c);
//				}
//			}
//			
//			if (key.endsWith(".ontolection")){
//				String collectionName = key.substring(0, key.indexOf(".ontolection"));
//				ontolectionMap.put(collectionName, (String)entry.getValue());
//				
//			}
//		}
//	}
	
	public CollectionShard getCollectionShardByName(String collectionShardName){
		return this.collectionShardMap.get(collectionShardName);
	}
	
	public String getOntolectionNameByCollection(String collectionName){
		return ontolectionMap.get(collectionName);
	}
	
	private String get(String key) {
		String value = this.prop.getProperty(key);
		if(value!=null){
			value=value.trim();
		}
		return value;
	}
	private int getInt(String key) {
		String value = get(key);
		int valueInt = 0;
		if(value!=null && value.length() > 0){
			valueInt = Integer.parseInt(value);
		}
		return valueInt;
	}

	public String getEnvironmentId() {
		return environmentId;
	}

	public Collection getCollectionByName(String collectionName) {
		return collectionMap.get(collectionName);
	}
}
