package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final AService aService;

    @GetMapping("/echo")
    public String echo(@RequestParam("str") String str) {
        aService.method();
        return str + str;
    }

}
