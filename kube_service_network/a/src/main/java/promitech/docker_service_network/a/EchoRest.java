package promitech.docker_service_network.a;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class EchoRest {

    @Autowired
    private BClient bClient;
    @Autowired
    private CClient cClient;
    
    @RequestMapping("/echo/{str}")
    public String echo(@PathVariable("str") String str) {
        String r = bClient.echo(str)  + ":" + cClient.echo(str);
        return "a" + r + "a";
    }
    
}
