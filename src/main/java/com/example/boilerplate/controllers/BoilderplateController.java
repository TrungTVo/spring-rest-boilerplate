package com.example.boilerplate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boilerplate.commons.models.Response;
import com.example.boilerplate.commons.models.ResponseMetadata;
import com.example.boilerplate.services.BoilerplateService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping("boilerplate")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BoilderplateController {
	
	private Logger logger = LoggerFactory.getLogger(BoilderplateController.class);
	
	@Autowired
	private BoilerplateService service;
	
	@GetMapping("test")
	public ResponseEntity<Response<?>> test() {
		logger.info("Test message logging...");
		Response<?> messageRes = Response.builder()
				.data(this.service.getMessage())
				.metadata(ResponseMetadata.success("Successfully called test API"))
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(messageRes);
	}
	
	
	@ApiOperation(value = "Greet with message", notes = "Greet with given message string")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Greeting..."),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error")
	})
	@GetMapping(path = "greet/{message}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<?>> greet(@ApiParam(value = "Message to be sent for greeting", required = true)
			@PathVariable(value = "message") String message) {
		logger.info("Greet message logging...");
		Response<?> messageRes = Response.builder()
				.data(message)
				.metadata(ResponseMetadata.success("Successfully called greet API"))
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(messageRes);
	}
	
}
