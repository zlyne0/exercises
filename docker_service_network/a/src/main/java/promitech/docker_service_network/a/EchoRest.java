package promitech.docker_service_network.a;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@RestController
@RequestMapping("/rest")
public class EchoRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoRest.class);
    
    @Autowired
    private DiscoveryClient discoveryClient;    
    
    @RequestMapping("/echo/{str}")
    public String echo(@PathVariable("str") String str) {
        String uri = null;
        List<ServiceInstance> instances = discoveryClient.getInstances("docker_service_network.b");
        for (ServiceInstance si : instances) {
            System.out.println("si.getUri() "  + si.getUri());
            System.out.println("si.getHost() " + si.getHost());
            System.out.println("si.getPort() " + si.getPort());
            System.out.println("si.getServiceId() " + si.getServiceId());
            uri = si.getUri().toString();
        }
        
        RestTemplate restTemplate = new RestTemplate();
        String responseStr = restTemplate.getForObject(uri + "/b/rest/echo/" + str, String.class);
        return "a" + responseStr + "a";
    }
    
}
