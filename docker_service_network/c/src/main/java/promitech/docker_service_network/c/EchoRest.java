package promitech.docker_service_network.c;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class EchoRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoRest.class);
    
    @Autowired
    private DClient dClient;
    @Autowired
    private EClient eClient;
    
    @RequestMapping("/echo/{str}")
    public String echo(@PathVariable("str") String str) {
        String r = dClient.echo(str) + ":" + eClient.echo(str);
        return "c" + r + "c";
    }
    
}
