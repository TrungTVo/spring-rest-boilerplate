package com.example.boilerplate.commons.types;

import org.apache.commons.lang3.EnumUtils;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ResponseStatus {
	SUCCESS, ERROR;

	@JsonCreator
	public static ResponseStatus fromName(String name) {
		return EnumUtils.getEnumIgnoreCase(ResponseStatus.class, name);
	}

	private ResponseStatus() {
	}
}
