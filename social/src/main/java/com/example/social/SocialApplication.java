package com.example.social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class SocialApplication {
	public static void main(String[] args) {
		SpringApplication.run(SocialApplication.class, args);
	}
}
