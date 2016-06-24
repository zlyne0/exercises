package hello;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages={"promitech.web_angular_exp", "hello"})
@EntityScan(basePackages={"promitech.web_angular_exp.model", "hello"})
@ComponentScan(basePackages={"promitech.web_angular_exp", "hello"})
public class Example {

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter crlf = new CommonsRequestLoggingFilter();
        crlf.setIncludeClientInfo(true);
        crlf.setIncludeQueryString(true);
        crlf.setIncludePayload(true);
        return crlf;
    }    
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class, args);
    }
    
}
