package com.gs.api;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.gs.models.RequestInfo;
import com.gs.utils.FileAdapter;
import com.gs.utils.FileHandler;

@ApplicationPath("/api")
public class MainApp extends ResourceConfig {

	private static BlockingQueue<RequestInfo> queue;
//	Where to initialize consumer thread
	private Thread readerThread;

	public MainApp() {
		packages("com.gs.api");
		register(MultiPartFeature.class);
		queue = new LinkedBlockingQueue<>(2);
		readerThread = new Thread(() -> {
			try {
				while (true) {
					RequestInfo requestInfo = queue.take();
					FileAdapter adapter = new FileAdapter();
					FileHandler handler = adapter.getFileHandler(requestInfo);
					new Thread(handler).start();
					handler.readFile();
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		readerThread.start();

	}

	public static void addToQueue(RequestInfo requestInfo) {
		try {
			queue.put(requestInfo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void finalize() {
		readerThread.interrupt();
	}

}
