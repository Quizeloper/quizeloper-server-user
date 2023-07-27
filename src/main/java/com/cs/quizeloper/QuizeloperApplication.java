package com.cs.quizeloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class QuizeloperApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizeloperApplication.class, args);
	}

}
