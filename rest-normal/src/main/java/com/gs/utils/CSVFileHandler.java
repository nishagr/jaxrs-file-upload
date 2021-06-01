package com.gs.utils;

import java.io.IOException;
import java.util.Arrays;

import com.gs.models.RequestInfo;
import com.opencsv.CSVReader;

public class CSVFileHandler extends FileHandler {

	public CSVFileHandler(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	@Override
	public void readFile() {
		System.out.println("CSV readFile Called");
		try {
			CSVReader reader = new CSVReader(new java.io.FileReader(requestInfo.getFilePath()));
			String[] record = null;
			while ((record = reader.readNext()) != null) {
				super.addToQueue(Arrays.asList(record));
			}
			while (!isQueueEmpty())
				;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		isActive = false;
	}

}