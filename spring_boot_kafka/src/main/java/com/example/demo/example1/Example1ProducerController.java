package com.example.demo.example1;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
class Example1ProducerController {

    private final Example1ProducerService example1ProducerService;

    @RequestMapping(path = "/example1/produce")
    public String produce() {
        example1ProducerService.produce();
        return "ok";
    }
}
