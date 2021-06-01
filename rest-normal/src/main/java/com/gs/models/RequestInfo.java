package com.gs.models;

import java.time.LocalDate;
import java.util.UUID;

public class RequestInfo {

	private LocalDate createdAt;
	private String transactionId;
	private String fileName;
	private String filePath;
	private String fileType;

	public RequestInfo(String fileName, String filePath, String fileType) {
		super();
		this.createdAt = LocalDate.now();
		this.transactionId = UUID.randomUUID().toString();
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileType = fileType;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}