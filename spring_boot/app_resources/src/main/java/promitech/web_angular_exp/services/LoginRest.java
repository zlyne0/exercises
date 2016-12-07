package promitech.web_angular_exp.services;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginRest {

    @RequestMapping("/rest/user/login")
    public Principal user(Principal user) {
        return user;
    }
    
}
