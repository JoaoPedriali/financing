package com.pedro.financing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJpaRepositories
public class FinancingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancingApplication.class, args);
	}

}
