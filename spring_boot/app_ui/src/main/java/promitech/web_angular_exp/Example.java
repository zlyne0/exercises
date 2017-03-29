package promitech.web_angular_exp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@SpringBootApplication
@Configuration
@EnableZuulProxy
@EnableOAuth2Sso
@ComponentScan(basePackages={"promitech.web_angular_exp", "hello"})
public class Example extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.logout()
            .and()
                .authorizeRequests()
                .antMatchers("/index.html", "/", "/styles/**/*.css", "/scripts/**/*.js", "/login").permitAll()
                .anyRequest().authenticated()
            .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
     
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class, args);
    }
    
}
