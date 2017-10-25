package com.ibm.services.tools.wexws.loadbalance;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.services.tools.wexws.WexConfiguration;
import com.ibm.services.tools.wexws.collections.CollectionShard;
import com.ibm.services.tools.wexws.collections.Server;

public class CollectionServerRoundRobin {
	
	private Map<Integer,ServerLoad> serverLoadMap;
	
	public CollectionServerRoundRobin(WexConfiguration config){
		List<Server> servers = config.getServers();
		this.serverLoadMap = getServerLoadMap(servers);
	}
	
	private Map<Integer, ServerLoad> getServerLoadMap(List<Server> servers) {
		Map<Integer, ServerLoad> serverLoadMap = new HashMap<Integer,ServerLoad>();
		
		for(Server server : servers) {
			ServerLoad serverLoad = new ServerLoad(server);
			serverLoadMap.put(server.getId(), serverLoad);
		}
		
		return serverLoadMap;
	}

	public Server getNextServer(CollectionShard collectionShard){
		return getNextServer(collectionShard.getServers());
	}
	
	public Server getNextServer(List<Server> servers){
		ServerLoad assignedServerLoad = null;
		for(Server server : servers){
			if(assignedServerLoad==null){
				assignedServerLoad=this.serverLoadMap.get(server.getId());
			}else{
				ServerLoad serverLoad = this.serverLoadMap.get(server.getId());
				if(serverLoad.getCount()<assignedServerLoad.getCount()){
					assignedServerLoad=serverLoad;
				}
			}
		}
			
		if(assignedServerLoad!= null) {
			assignedServerLoad.increment();
			return assignedServerLoad.getServer();
		}
		
		return null;

	}
	
}
