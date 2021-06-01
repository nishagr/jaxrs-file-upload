package com.gupshup.api;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class MainApp extends ResourceConfig {
	public static ExecutorService producerExecutor;
	public static ExecutorService consumerExecutor;
	public static BlockingQueue<List<String>> queue;

	public MainApp() {
		System.out.println("Hello Main App");
		packages("com.gupshup.api");
		register(MultiPartFeature.class);
		producerExecutor = Executors.newCachedThreadPool();
		consumerExecutor = Executors.newSingleThreadExecutor();
		queue = new LinkedBlockingDeque<>(1);
		RowConsumer consumer = new RowConsumer(queue);
		consumerExecutor.execute(consumer);
	}

	public void finalize() throws Throwable {
		consumerExecutor.shutdown();
	}

}
