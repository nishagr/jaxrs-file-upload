package com.gs.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gs.models.RequestInfo;

public class ExcelFileHandler extends FileHandler {

	public ExcelFileHandler(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	@Override
	public void readFile() {
		System.out.println("Excel readFile Called");
		try {
			String filePath = requestInfo.getFilePath();
			FileInputStream fileInputStream = new FileInputStream(new File(filePath));
			Sheet sheet = null;
			switch (filePath.substring(filePath.lastIndexOf('.') + 1)) {
			case "xls":
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fileInputStream);
				sheet = hssfWorkbook.getSheetAt(0);
				break;
			case "xlsx":
				XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
				sheet = xssfWorkbook.getSheetAt(0);
				break;
			}
			Iterator<Row> itr = sheet.iterator(); // iterating over excel file
			while (itr.hasNext()) {
				Row row = itr.next();
				List<String> record = new ArrayList<>();
				Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each column
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case STRING: // field that represents string cell type
						record.add(cell.getStringCellValue());
						break;
					case NUMERIC: // field that represents number cell type
						record.add(String.valueOf(cell.getNumericCellValue()));
						break;
					default:
						break;
					}
				}
				addToQueue(record);
			}
			while (!isQueueEmpty())
				;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isActive = false;

	}

}