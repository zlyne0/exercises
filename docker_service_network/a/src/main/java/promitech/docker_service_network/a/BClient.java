package promitech.docker_service_network.a;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("docker.service.network.b")
interface BClient {
    @RequestMapping(method = RequestMethod.GET, value = "/b/rest/echo/{str}")
    String echo(@PathVariable("str") String str);
}