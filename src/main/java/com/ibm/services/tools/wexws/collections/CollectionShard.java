package com.ibm.services.tools.wexws.collections;

import java.util.List;

public class CollectionShard {
	
	private String shardName;
	private List<Server> servers;
	private Collection collection;
	
	CollectionShard(String shardName, Collection collection, List<Server> servers) {
		super();
		this.shardName = shardName;
		this.collection = collection;
		this.servers = servers;
	}
	
	public String getShardName() {
		return shardName;
	}

	public List<Server> getServers() {
		return servers;
	}

	public Collection getCollection() {
		return collection;
	}
	
	public Server getAlternativeServer(Server failedServer) {
		for (Server server : servers) {
			if (!server.getAddress().equals(failedServer.getAddress()) && !server.hasFailedAttempt()) {
				return server;
			}
		}
		return getAlternativeServerEvenIfFailed(failedServer);
	}

	private Server getAlternativeServerEvenIfFailed(Server failedServer) {
		for (Server server : servers) {
			if (!server.getAddress().equals(failedServer.getAddress())) {
				return server;
			}
		}
		return null;
	}
	

}
