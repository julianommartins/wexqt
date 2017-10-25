package com.ibm.services.tools.wexws.factory;

import java.util.HashMap;
import java.util.Map;

import com.ibm.services.tools.wexws.WexConfiguration;
import com.ibm.services.tools.wexws.dao.WexRestfulDAO;




public class WexRestfulFactory {

	//private static Logger logger = Logger.getLogger(WexRestfulFactory.class);
	
	private static Map<String,WexRestfulDAO> daoMap = new HashMap<String,WexRestfulDAO>();
	
	
	public static WexRestfulDAO getInstance(String environmentId) {
		
		WexRestfulDAO dao = daoMap.get(environmentId);
		if(dao==null){
			WexConfiguration config = new WexConfiguration(environmentId);
			WexUrlFactory urlFactory = new WexUrlFactory(config);
			dao = new WexRestfulDAO(urlFactory);
			daoMap.put(environmentId, dao);
		}
		
		return dao;
	}


}
