package com.gs.utils;

import com.gs.models.RequestInfo;

public class FileAdapter {
	public FileHandler getFileHandler(RequestInfo requestInfo) {
		if (requestInfo.getFileType() == null)
			return null;
		switch (requestInfo.getFileType().toLowerCase()) {
		case "csv":
			return new CSVFileHandler(requestInfo);
		case "xls":
		case "xlsx":
			return new ExcelFileHandler(requestInfo);
		default:
			return null;
		}
	}
}
