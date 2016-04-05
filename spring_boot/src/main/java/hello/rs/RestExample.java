package hello.rs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.Application;
import hello.Customer;
import hello.CustomerRepository;


@RestController
public class RestExample {
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	
    @RequestMapping("/hello")
    String home() {
		log.info("Customers found with findAll():");
		log.info("-------------------------------");
		for (Customer customer : customerRepository.findAll()) {
			log.info(customer.toString());
		}
        log.info("");
    	
        return "Hello World!";
    }
   
}
