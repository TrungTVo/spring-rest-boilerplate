package com.example.boilerplate.commons.models;

import java.util.List;

import com.example.boilerplate.commons.types.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMetadata {
	private Integer statusCode;
	private String status;
	private String message;
	private List<String> errors;

	public static ResponseMetadata success(String message, Integer statusCode) {
		return ResponseMetadata.builder()
				.statusCode(statusCode)
				.status(ResponseStatus.SUCCESS.getStatus())
				.message(message)
				.build();
	}
}
