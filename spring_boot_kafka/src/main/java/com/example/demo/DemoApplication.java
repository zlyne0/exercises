package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


//	@Bean
//	public NewTopic createTopic() {
//		return new NewTopic(Conf.TOPIC_NAME, 1, (short)1);
//	}

//	@Bean
//	public ApplicationRunner run() {
//		return new ApplicationRunner() {
//			@Override
//			public void run(ApplicationArguments args) throws Exception {
//				myBean.produce();
//			}
//		};
//	}

}
