package com.zaf.netty_study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class SpringBootExampleApplication {

	static {

		String userDir = System.getProperty("user.dir");
		System.getProperties().put("spring.property.path", userDir + "/src/main");
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootExampleApplication.class, args);
	}
}
