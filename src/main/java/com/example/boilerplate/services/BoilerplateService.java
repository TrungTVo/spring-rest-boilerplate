package com.example.boilerplate.services;

import org.springframework.stereotype.Service;

import com.example.boilerplate.exceptions.NoRecordFoundException;

@Service
public class BoilerplateService {
	
	public String getMessage() {
		if (true) {
			throw new NoRecordFoundException();
		}
		return "Test message here...";
	}
}
