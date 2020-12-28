package promitech.docker_service_network.a;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "docker.service.network.c", url = "${service_c_url}")
interface CClient {
    @RequestMapping(method = RequestMethod.GET, value = "/rest/echo/{str}")
    String echo(@PathVariable("str") String str);
}