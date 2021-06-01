package com.gupshup.api;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class RowConsumer implements Runnable {

	private BlockingQueue<List<String>> queue;

	RowConsumer(BlockingQueue<List<String>> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				List<String> record = queue.poll();
				if (record != null) {
					for (String i : record)
						System.out.print(i + "\t\t\t");
					System.out.println("");

				} else {
					System.out.println("Sleeping");
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}