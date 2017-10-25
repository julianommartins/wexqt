package com.ibm.services.tools.wexws.factory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ibm.services.tools.wexws.WexWsConstants;
import com.ibm.services.tools.wexws.bo.SmartConditionDataProvider;
import com.ibm.services.tools.wexws.bo.WexSmartConditionDataProvider;

/**
 * This class is responsible to control the instances for the WexSmartConditionDataProvider class 
 * and to control how often the classes will be reloaded.
 * @author Daniel Paganotti
 *
 */
public class WexSmartConditionDataProviderFactory {

	private static final Logger logger = Logger.getLogger(WexSmartConditionDataProviderFactory.class);
	
	private static final Object lock = new Object();
	private static ConcurrentHashMap<String,SmartConditionDataProvider> instancesMap = new ConcurrentHashMap<String,SmartConditionDataProvider>();
	private static final ScheduledExecutorService reloadDataExecutorService = Executors.newScheduledThreadPool(1, new WexSmartConditionThreadFactory());
	static{
		reloadDataExecutorService.scheduleAtFixedRate(new ReloadDataRunnable(), 4, 4, TimeUnit.HOURS);
	}
		
	public static SmartConditionDataProvider getWexDataProviderInstance(String environment){
		SmartConditionDataProvider instance = instancesMap.get(environment);
		if (instance == null){
			synchronized (lock) {
				instance = instancesMap.get(environment);
				// this check is needed in case another thread got the
				// lock just before this thread entered the synchronized area
				if (instance == null) {
					logger.info("Initializing WEX-WS API R " + WexWsConstants.APIRELEASE + " - "+environment);
					instance = new WexSmartConditionDataProvider(WexRestfulFactory.getInstance(environment));
					instancesMap.put(environment, instance);
				}
			}
		}
		return instance;
	}
	
	private static class ReloadDataRunnable implements Runnable{

		@Override
		public void run() {
			for (String key: instancesMap.keySet()){
				logger.info("About to reload WexSmartConditionDataProvider instance for environment="+key);
				
				instancesMap.put(key, new WexSmartConditionDataProvider(WexRestfulFactory.getInstance(key)));
				
				logger.info("Finished reloading WexSmartConditionDataProvider instance for environment="+key);
			}
		}
	}
	
	private static class WexSmartConditionThreadFactory implements ThreadFactory{
		
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setName("WexSmartCondition reload thread");
			thread.setDaemon(true);
			return thread;
		}
		
	}
}
