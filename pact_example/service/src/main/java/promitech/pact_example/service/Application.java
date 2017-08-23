package promitech.pact_example.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = {"promitech.pact_example.service"})
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
}
