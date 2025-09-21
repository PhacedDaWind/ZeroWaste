package com.example.zerowaste_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ZerowasteApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZerowasteApiApplication.class, args);
	}

}
