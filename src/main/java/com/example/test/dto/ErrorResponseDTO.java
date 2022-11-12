package com.example.test.dto;

public class ErrorResponseDTO {
	private String timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
	
	public String getTimestamp() {
		return timestamp;
	}
	public Integer getStatus() {
		return status;
	}
	public String getError() {
		return error;
	}
	public String getMessage() {
		return message;
	}
	public String getPath() {
		return path;
	}

}
