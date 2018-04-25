package promitech.docker_service_network.c;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("docker.service.network.d")
interface DClient {
    @RequestMapping(method = RequestMethod.GET, value = "/d/rest/echo/{str}")
    String echo(@PathVariable("str") String str);
}