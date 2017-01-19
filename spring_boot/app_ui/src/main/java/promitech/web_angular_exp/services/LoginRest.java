package promitech.web_angular_exp.services;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginRest {

    @RequestMapping("/session/user/login")
    public Principal user(Principal user) {
        return user;
    }
    
    @RequestMapping("/session/token")
    public Map<String, String> token(HttpSession session) {
        return Collections.singletonMap("token", session.getId());
    }
}
