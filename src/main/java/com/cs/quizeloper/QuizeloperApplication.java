package com.cs.quizeloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class QuizeloperApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizeloperApplication.class, args);
	}

}