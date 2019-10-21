package org.revolut.moneytransfer.dao;

import java.util.concurrent.ConcurrentHashMap;

import org.revolut.moneytransfer.model.UserAcount;


public class InMemoryDataStore {
	
	public static ConcurrentHashMap<Long, UserAcount> map= new ConcurrentHashMap<>();
	
	static
	{
		map.put(1L, new UserAcount(1, "TEST1", "100"));
		map.put(2L,new UserAcount(2, "TEST2", "100"));
	}

	public static ConcurrentHashMap<Long, UserAcount> getMap() {
		return map;
	}

	public static void setMap(ConcurrentHashMap<Long, UserAcount> map) {
		InMemoryDataStore.map = map;
	}
	
	
}
