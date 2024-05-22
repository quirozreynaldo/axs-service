package com.keysolbo.axsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AxsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AxsServiceApplication.class, args);
	}

}
