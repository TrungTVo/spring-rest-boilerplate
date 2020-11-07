package com.example.boilerplate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boilerplate.commons.models.Response;
import com.example.boilerplate.commons.models.ResponseMetadata;
import com.example.boilerplate.services.BoilerplateService;


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
		Response messageRes = Response.builder()
				.data(this.service.getMessage())
				.metadata(ResponseMetadata.success("Successfully called test API"))
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(messageRes);
	}
	
}
