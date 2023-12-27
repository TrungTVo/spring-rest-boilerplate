package com.example.boilerplate.commons.types;

public enum ResponseStatus {
	SUCCESS("Success"),
	ERROR("Error");

	private final String status;

	ResponseStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}
}
