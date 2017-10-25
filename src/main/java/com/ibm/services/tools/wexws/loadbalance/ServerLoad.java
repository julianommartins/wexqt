package com.ibm.services.tools.wexws.loadbalance;

import java.util.concurrent.atomic.AtomicInteger;

import com.ibm.services.tools.wexws.collections.Server;

public class ServerLoad {
	
	private Server server;
	private AtomicInteger count;
	

	public ServerLoad(Server server) {
		this.server = server;
		this.count = new AtomicInteger();
	}

	public int getCount(){
		return count.get();
	}
	
	public int increment(){
		return this.count.incrementAndGet();
	}

	public Server getServer() {
		return server;
	}

}
