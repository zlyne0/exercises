package com.example.demo.example3;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
class Example3ProducerController {

    private final Example3ProducerService example3ProducerService;

    @RequestMapping(path = "/example3/produce")
    public String produce() {
        example3ProducerService.produce();
        return "ok";
    }
}
