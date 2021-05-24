package com.gupshup.rest;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

// start URL = http://localhost:9090/rest-maven/

@Path("/file")
public class FileUpload {

	private ExecutorService executor;
	private BlockingQueue<List<String>> queue;
	public static boolean isActive = false;

	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		executor = Executors.newFixedThreadPool(2);
		queue = new LinkedBlockingDeque<>(1);
		isActive = true;
		RowConsumer consumer = new RowConsumer(queue);
		executor.execute(consumer);
		String directoryName = Paths.get("").toAbsolutePath().toString() + "/test-gupshup/";
		String fileName = fileDetail.getFileName();
		String filePath = directoryName.concat(fileName);
		File directory = new File(directoryName);
		if (!directory.exists())
			directory.mkdirs();
		FileWriter fileWriter = new FileWriter();
		fileWriter.writeToFile(uploadedInputStream, filePath);
		return readFile(filePath).build();
	}

	private ResponseBuilder readFile(String filePath) {

		List<String> validExtns = Arrays.asList("csv", "xls", "xlsx", "zip");
		int extensionIndex = filePath.lastIndexOf('.');
		String extension = filePath.substring(extensionIndex + 1);
		if (extensionIndex != -1 && validExtns.contains(extension)) {
			switch (extension) {
			case "zip":
				ZipFileHandler zfh = new ZipFileHandler();
				int filesCount = zfh.countFilesInZip(filePath);
				if (filesCount != 1)
					return Response.status(400).entity("Zip file should contain only 1 file");
				String extractedFilePath = zfh.readZipFile(filePath, filePath.substring(0, filePath.lastIndexOf('.')));
				return readFile(extractedFilePath);
			case "csv":
				CSVFile csvFile = new CSVFile(queue, filePath);
				executor.execute(csvFile);
				break;
			case "xls":
			case "xlsx":
				ExcelFile excelFile = new ExcelFile(queue, filePath);
				executor.execute(excelFile);
				break;
			}
			return Response.status(200).entity("." + extension + " file successfully uploaded at " + filePath);
		}
		isActive = false;
		executor.shutdown();
		return Response.status(400).entity("Invalid File, Files can only be .csv, .xlsx, .xls and .zip");

	}

}