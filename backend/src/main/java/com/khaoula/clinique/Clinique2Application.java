package com.khaoula.clinique;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.khaoula.clinique")
@EntityScan(basePackages = "com.khaoula.clinique.entities")
@EnableJpaRepositories(basePackages = "com.khaoula.clinique.repos")
public class Clinique2Application {

	public static void main(String[] args) {
		SpringApplication.run(Clinique2Application.class, args);
	}

}