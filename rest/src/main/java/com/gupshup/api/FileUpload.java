package com.gupshup.api;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/file")
public class FileUpload {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response test() {
		return Response.status(200).entity("It Works, send a POST request to upload a file !!").build();
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed("admin")
	public Response upload(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		String directoryName = Paths.get("").toAbsolutePath().toString() + "/test-gupshup/";
		String fileName = fileDetail.getFileName();
		String filePath = directoryName.concat(fileName);
		List<String> validExtns = Arrays.asList("csv", "xls", "xlsx", "zip");
		int extensionIndex = fileName.lastIndexOf('.');
		String extension = fileName.substring(extensionIndex + 1);
		if (extensionIndex != -1 && validExtns.contains(extension)) {
			File directory = new File(directoryName);
			if (!directory.exists())
				directory.mkdirs();
			FileWriter fileWriter = new FileWriter();
			fileWriter.writeToFile(uploadedInputStream, filePath);
			if (extension.equals("zip")) {
				filePath = extractZipFile(filePath);
			}
			if (filePath != "")
				return readFile(filePath).build();
		}
		return Response.status(400).entity("Invalid File, Files can only be .csv, .xlsx, .xls and .zip").build();
	}

	private String extractZipFile(String filePath) {
		String extractedFilePath = "";
		ZipFileHandler zfh = new ZipFileHandler();
		int filesCount = zfh.countFilesInZip(filePath);
		if (filesCount == 1)
			extractedFilePath = zfh.readZipFile(filePath, filePath.substring(0, filePath.lastIndexOf('.')));
		return extractedFilePath;
	}

	private ResponseBuilder readFile(String filePath) {

		List<String> validExtns = Arrays.asList("csv", "xls", "xlsx");
		int extensionIndex = filePath.lastIndexOf('.');
		String extension = filePath.substring(extensionIndex + 1);
		if (extensionIndex != -1 && validExtns.contains(extension)) {
			switch (extension) {
			case "csv":
				CSVFile csvFile = new CSVFile(MainApp.queue, filePath);
				MainApp.producerExecutor.execute(csvFile);
				break;
			case "xls":
			case "xlsx":
				ExcelFile excelFile = new ExcelFile(MainApp.queue, filePath);
				MainApp.producerExecutor.execute(excelFile);
				break;
			}
			return Response.status(200).entity("." + extension + " file successfully uploaded at " + filePath);
		}
		return Response.status(400);

	}
}
