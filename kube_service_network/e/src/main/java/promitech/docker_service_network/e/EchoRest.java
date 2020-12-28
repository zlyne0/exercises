package promitech.docker_service_network.e;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class EchoRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoRest.class);
    
    @RequestMapping("/echo/{str}")
    public String echo(@PathVariable("str") String str) {
        return "e" + str + "e";
    }
    
}
