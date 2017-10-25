package com.ibm.services.tools.wexws.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Collection {
	
	private String collectionName;
	//private List<Integer> serversIds;
	private List<Server> servers;
	private List<CollectionShard> shards;
	
	private Collection(String collectionName) {
		super();
		this.collectionName = collectionName;
	}

//	public Collection(String collectionName, List<Integer> serversIds) {
//		super();
//		this.collectionName = collectionName;
//		this.serversIds = serversIds;
//	}
	
	public String getCollectionName() {
		return collectionName;
	}

//	public List<Integer> getServersIds() {
//		return serversIds;
//	}


	public List<Server> getServers() {
		return servers;
	}

	public List<CollectionShard> getShards() {
		return shards;
	}
	
	public static CollectionBuilder getBuilder(String collectionName) {
		return new CollectionBuilder(collectionName);
	}
	
	public static class CollectionBuilder {
		
		private Collection newCollection;
		private Map<String, List<Server>> serversByShardName;
		private Set<Server> servers;
		
		public CollectionBuilder(String collectionName) {
			newCollection = new Collection(collectionName);
			serversByShardName = new HashMap<String, List<Server>>();
			servers = new HashSet<Server>();
		}
		 
		public CollectionBuilder withShardOnServer(String shardName, Server server) {
			List<Server> servers = serversByShardName.get(shardName);
			if (servers == null) {
				servers = new ArrayList<Server>();
				serversByShardName.put(shardName, servers);
			}
			servers.add(server);
			this.servers.add(server);
			
			return this;
		}
		
		public Collection build() {
			
			newCollection.servers = new ArrayList<Server>();
			newCollection.shards = new ArrayList<CollectionShard>();
			
			for (String shardName : serversByShardName.keySet()) {	
				List<Server> shardServers = serversByShardName.get(shardName);
				CollectionShard shard = new CollectionShard(shardName, newCollection, shardServers);
				newCollection.shards.add(shard);
				
				for (Server server : shardServers) {
					server.addShard(shard);
				}
			}
			
			for (Server server : servers) {
				newCollection.servers.add(server);
			}
			return newCollection;
		}
		
	}
	

}
