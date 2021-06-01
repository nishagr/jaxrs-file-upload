package com.gupshup.rest;

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
		while (FileUpload.isActive) {
			try {
				List<String> record = queue.take();
//				for (String i : record)
//					System.out.print(" | " + i);
//				System.out.println(" |");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
