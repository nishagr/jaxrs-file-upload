package com.gupshup.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipFileHandler {
	public int countFilesInZip(String fileLocation) {
		int numFiles = 0;
		try {
			ZipFile zipFile = new ZipFile(new File(fileLocation));
			final Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements())
				if (!entries.nextElement().isDirectory())
					++numFiles;
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numFiles;
	}

	public String readZipFile(String fileLocation, String destination) {
		try {
			File destDir = new File(destination);
			if (!destDir.exists())
				destDir.mkdir();

			ZipInputStream zipIn = new ZipInputStream(new FileInputStream(fileLocation));
			ZipEntry entry = zipIn.getNextEntry();
			// iterates over entries in the zip file
			while (entry != null) {
				String filePath = destination + File.separator + entry.getName();
				if (!entry.isDirectory()) {
					FileWriter fileWriter = new FileWriter();
					fileWriter.writeToFile(zipIn, filePath);
				} else {
					// if the entry is a directory, make the directory
					File dir = new File(filePath);
					dir.mkdirs();
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
			zipIn.close();
			File file = new File(destination).listFiles()[0];
			while (file.isDirectory())
				file = file.listFiles()[0];
			return file.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
