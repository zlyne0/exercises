package promitech.docker_service_network.c;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "docker.service.network.e", url = "${service_e_url}")
interface EClient {
    @RequestMapping(method = RequestMethod.GET, value = "/rest/echo/{str}")
    String echo(@PathVariable("str") String str);
}