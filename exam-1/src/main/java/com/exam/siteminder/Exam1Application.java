package com.exam.siteminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class Exam1Application {

	
	public static void main(String[] args) {
		SpringApplication.run(Exam1Application.class, args);
	}

}
