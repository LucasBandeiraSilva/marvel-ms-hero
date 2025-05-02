package com.github.lucasbandeira.msmarvel;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class MsmarvelApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsmarvelApplication.class, args);
	}

}
