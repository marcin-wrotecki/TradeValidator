package com.ubs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket get() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.ubs.controller")).build().apiInfo(createApiInfo());
	}

	private ApiInfo createApiInfo() {
		return new ApiInfoBuilder().title("Trades API").description("The app that allows you to validate FX trades")
				.version("1.0.0").contact(new Contact("Marcin Wrotecki", "\"https://github.com/marcin-wrotecki\"",
						"marcin.wrotecki@gft.com"))
				.license("My own license").build();
	}
}