package com.example.demo;


import io.micrometer.core.instrument.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class NewsController {

    private Counter counter;
    private MeterRegistry registry;

    public NewsController(MeterRegistry registry) {
        this.registry = registry;
        // counter
        this.counter = Counter.builder("news_fetch_request_total")
                .tag("version", "v1")
                .description("News Fetch Count")
                .register(registry);

        Gauge.builder("temperature", () -> {
            return counter.count();
//                    System.out.println("temperature called");
//                    double min = 10, max = 30;
//                    return min + new Random().nextDouble() * (max - min);
                })
                .tag("version", "v1")
                .description("Temperature Record")
                .register(registry);

        TimeGauge.builder("mytimegauge", () -> {
                            return counter.count();

//                            System.out.println("my time gauge called");
//                    int min = 10, max = 30;
//                    return min + new Random().nextInt() * (max - min);
                }, TimeUnit.HOURS
                )
                .description("my  time gauge")
                .register(registry);
    }

    @GetMapping("/news")
    public List<News> getNews() {
//        Timer timer = registry.timer("water.by.size", Tags.of("tag", Double.toString(counter.count())));
//        Timer timer = registry.timer("water.by.size");
//
//        timer.record(()-> {
//            try {
//                Thread.sleep(100 + new Random().nextInt(600));
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });

        counter.increment();

        return List.of(new News("Good News!"), new News("Bad News!"));
    }

    public record News(String title) {
    }
}
