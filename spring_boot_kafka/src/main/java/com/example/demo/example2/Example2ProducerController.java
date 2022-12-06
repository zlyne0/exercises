package com.example.demo.example2;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
class Example2ProducerController {

    private final Example2ProducerService example2ProducerService;

    @RequestMapping(path = "/example2/produce")
    public String produce() {
        example2ProducerService.produce();
        return "ok";
    }
}
