package com.ibm.services.tools.wexws.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThreadExecutorMaganer {

	private ExecutorService executor;
	private long timeout;

	private List<Callable<String>> callables;

	public ThreadExecutorMaganer(int maxThreads, long timeoutInSeconds) {
		this.executor = Executors.newFixedThreadPool(maxThreads);
		this.callables = new ArrayList<Callable<String>>();
		this.timeout = timeoutInSeconds;
	}

	public void add(Callable<String> callable) {
		this.callables.add(callable);
	}

	public List<Future<String>> start() {
		List<Future<String>> futures = null;

		try {
			futures = executor.invokeAll(callables, timeout, TimeUnit.SECONDS);

			executor.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();

			executor.shutdownNow();
		}

		return futures;
	}

}
