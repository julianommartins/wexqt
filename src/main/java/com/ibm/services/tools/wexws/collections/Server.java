package com.ibm.services.tools.wexws.collections;

import java.util.ArrayList;
import java.util.List;

public class Server {
	
	private int id;
	private String address;
	private List<CollectionShard> shards;
	private boolean failedAttempt;
	
	public Server(int id, String address) {
		super();
		this.id = id;
		this.address = address;
		this.failedAttempt = false;
	}
	public int getId() {
		return id;
	}
	public String getAddress() {
		return address;
	}
	public List<CollectionShard> getShards() {
		return shards;
	}
	public boolean hasFailedAttempt() {
		return failedAttempt;
	}
	public void markFailedAttempt() {
		this.failedAttempt = true;
	}
	
	void addShard(CollectionShard shard) {
		if (this.shards == null) {
			this.shards = new ArrayList<CollectionShard>();
		}
		
		this.shards.add(shard);
	}

	
}
