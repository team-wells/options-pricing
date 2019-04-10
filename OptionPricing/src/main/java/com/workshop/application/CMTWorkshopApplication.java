package com.workshop.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"com.workshop"})
public class CMTWorkshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(CMTWorkshopApplication.class, args);
	}
}
