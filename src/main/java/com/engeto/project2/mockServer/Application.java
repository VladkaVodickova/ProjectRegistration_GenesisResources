package com.engeto.project2.mockServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.engeto.project2.controller")
@ComponentScan(basePackages = "com.engeto.project2.service")
@ComponentScan(basePackages = "com.engeto.project2.repository")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
