package com.nibbsinstantpaymenttest.config;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ComponentScan
public class SwaggerConfiguration {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).protocols(new HashSet<>(Arrays.asList("HTTP")))
				.apiInfo(metaData()).select().apis(RequestHandlerSelectors.basePackage("com.nibbsinstantpaymenttest"))
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("Project Nibbs Instant Payment System Challenge").description(
				"REST API for Nibbs Instant Payment System. " + "Refer to use case documentation for more information")
				.build();
	}

}
