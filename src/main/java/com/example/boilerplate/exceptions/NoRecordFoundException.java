package com.example.boilerplate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoRecordFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 7993659193328383333L;
	
	protected static final String DEFAULT_MESSAGE = "No record found";
	
	public NoRecordFoundException() {
		super(DEFAULT_MESSAGE);
	}
	
	public NoRecordFoundException(String message) {
		super(message);
	}
	
	public NoRecordFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
