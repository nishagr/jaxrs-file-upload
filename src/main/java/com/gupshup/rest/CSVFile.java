package com.gupshup.rest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.opencsv.CSVReader;

public class CSVFile implements Runnable {

	private BlockingQueue<List<String>> queue;
	private String filePath;

	public CSVFile(BlockingQueue<List<String>> queue, String filePath) {
		this.queue = queue;
		this.filePath = filePath;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			CSVReader reader = new CSVReader(new java.io.FileReader(filePath));
			String[] record = null;
			while ((record = reader.readNext()) != null) {
				try {
					queue.put(Arrays.asList(record));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
