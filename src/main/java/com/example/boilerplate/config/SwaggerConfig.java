package com.example.boilerplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI apiConfiguration() {
		return new OpenAPI()
				.info(new Info()
						.title("Demo REST API")
						.description("Spring REST sample application")
						.version("v0.0.1")
						.license(new License().name("Apache 2.0").url("http://springdoc.org")))
				.externalDocs(
						new ExternalDocumentation()
								.description("Github repo link")
								.url("https://github.com/TrungTVo/spring-rest-boilerplate.git"));
	}

}
