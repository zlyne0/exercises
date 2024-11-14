package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainProducer {

	public static void main(String[] args) {
		SpringApplication.run(MainProducer.class, args);
	}

	@Autowired
	private Sender sender;

	@Bean
	public CommandLineRunner usage() {
		return args -> {
			System.out.println("This app uses Spring Profiles to");
			sender.send();
		};
	}

}
