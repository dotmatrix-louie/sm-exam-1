package com.exam.siteminder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.exam.siteminder.controller.EmailController;

@SpringBootTest
class Exam1ApplicationTests {
	
	@Autowired
	private EmailController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
