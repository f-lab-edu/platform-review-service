package com.library.validate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ValidateLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidateLibraryApplication.class, args);
	}

}
