package com.example.demo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

class AsyncService {

    @Async("myAsyncPoolName")
    public CompletableFuture<String> runInAsync() {
        perform();
        return CompletableFuture.completedFuture("winter is coming");
    }

    private void perform() {
        System.out.println("AsyncService waiting 3 sec");
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
        }
        System.out.println("AsyncService end waiting");
    }

    @Async("myAsyncPoolName")
    public CompletableFuture<String> runAndThrowException() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("exception for completable future");
    }

}

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class SpringBootAsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAsyncApplication.class, args);
    }

    @Bean
    AsyncService asyncService() {
        return new AsyncService();
    }

//    @Bean
//    MyScheduler myScheduler() {
//        return new MyScheduler();
//    }

    @Bean(name = "myAsyncPoolName")
    ThreadPoolTaskExecutor defaultExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(5);
        executor.setCorePoolSize(5);
        executor.setThreadNamePrefix("DefaultExecutorTask-");
        executor.setQueueCapacity(10);
        executor.initialize();
        return executor;
    }

    @Bean
    public CommandLineRunner demo(AsyncService asyncService) {
        return (args) -> {

            // jak bedzie wyjatek na completable future to przy get poleci
            {
                CompletableFuture<String> exceptionCompletableFuture = asyncService.runAndThrowException();
                Thread.sleep(1000);
                System.out.println("runAndThrowException iscanceled: " + exceptionCompletableFuture.isCancelled());
                System.out.println("runAndThrowException isCompletedExceptionally(): " + exceptionCompletableFuture.isCompletedExceptionally());



//                exceptionCompletableFuture.exceptionally(exception -> {
//                    System.out.println("log my exception");
//                    return "aa";
//                });
                exceptionCompletableFuture.whenComplete((value, exception) -> {
                    System.out.println("value : " + value);
                    System.out.println("exception : " + exception);
                }).thenAccept(v -> {
                    System.out.println("XXX thenAccept v " + v);
                });

                System.out.println("end on exceptionCompletableFuture");
            }

//            System.out.println("run run run");
//            CompletableFuture<String> strFuture = asyncService.runInAsync();
//            System.out.println("end end end");
//
//            Thread.sleep(1000);
//            System.out.println("waiting for future");
//            String waitingStr = strFuture.get();
//            System.out.println("waiting str: " + waitingStr);
        };
    }

}
