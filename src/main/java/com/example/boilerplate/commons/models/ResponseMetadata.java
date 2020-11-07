package com.example.boilerplate.commons.models;

import java.util.List;

import org.springframework.http.HttpStatus;

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
	private ResponseStatus status;
	private String message;
	private List<String> errors;

	public static ResponseMetadata success(String message) {
		return ResponseMetadata.builder()
				.statusCode(HttpStatus.OK.value())
				.status(ResponseStatus.SUCCESS)
				.message(message)
				.build();
	}
}
