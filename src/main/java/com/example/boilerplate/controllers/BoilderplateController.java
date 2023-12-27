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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
				.metadata(ResponseMetadata.success("Successfully called test API", HttpStatus.OK.value()))
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(messageRes);
	}

	@Operation(summary = "Greet with message", description = "Greet with given message string")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Greeting OK..."),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "404", description = "Not Found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@GetMapping(path = "greet/{message}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<?>> greet(
			@Parameter(name = "message", description = "Message to be sent for greeting", required = true, example = "hello world") @PathVariable(value = "message") String message) {
		logger.info("Greet message logging...");
		Response<?> messageRes = Response.builder()
				.data(message)
				.metadata(ResponseMetadata.success("Successfully called greet API", HttpStatus.OK.value()))
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(messageRes);
	}

}
