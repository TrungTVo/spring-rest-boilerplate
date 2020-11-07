package com.example.boilerplate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.boilerplate.commons.models.Response;
import com.example.boilerplate.commons.models.ResponseMetadata;
import com.example.boilerplate.commons.types.ResponseStatus;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@SuppressWarnings("rawtypes")
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> handleAllExceptions(Exception ex) {
		ResponseMetadata metadata = ResponseMetadata.builder()
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.status(ResponseStatus.ERROR)
				.message(ex.getMessage())
				.build();
		Response response = Response.builder().metadata(metadata).build();
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@SuppressWarnings("rawtypes")
	@ExceptionHandler(NoRecordFoundException.class)
	public ResponseEntity<Response> handleNoRecordFoundException(NoRecordFoundException ex) {
		ResponseMetadata metadata = ResponseMetadata.builder()
				.statusCode(HttpStatus.NOT_FOUND.value())
				.status(ResponseStatus.ERROR)
				.message(ex.getMessage())
				.build();
		Response response = Response.builder().metadata(metadata).build();
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
}
