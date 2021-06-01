package com.gs.utils;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.gs.models.RequestInfo;

public abstract class FileHandler implements Runnable {

	private BlockingQueue<List<String>> queue;
	protected boolean isActive;
	protected RequestInfo requestInfo;

	protected FileHandler() {
		isActive = true;
		queue = new LinkedBlockingQueue<>(1);
	}

	public void addToQueue(List<String> row) throws InterruptedException {
		queue.put(row);
	}

	public boolean isQueueEmpty() {
		return queue.isEmpty();
	}

	public abstract void readFile();

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.err.println(requestInfo.getTransactionId() + " Processing Started");
		while (isActive) {
			try {
				List<String> record = queue.take();
				for (String i : record)
					System.out.print(" | " + i);
				System.out.println(" |");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.err.println(requestInfo.getTransactionId() + " Processing Finished");
	}

}
