package com.example.demo;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;

public class MyScheduler {

    int mySchedulerRunCounter = 0;

    @Autowired
    ScheduledAnnotationBeanPostProcessor scheduledAnnotationBeanPostProcessor;

    @Scheduled(fixedDelayString = "5000", initialDelayString = "5000")
    public void run() {

        // jak poleci wyjatek to timer czeka i znowu sie odpala
//        if (true) {
//            throw new IllegalStateException("my exception from scheduler");
//        }

        System.out.println("MyScheduler run : " + (mySchedulerRunCounter++));
        Set<ScheduledTask> scheduledTasks = scheduledAnnotationBeanPostProcessor.getScheduledTasks();

        System.out.println("MyScheduler task count: " + scheduledTasks.size());
        for (ScheduledTask scheduledTask : scheduledTasks) {
            System.out.println("" + scheduledTask.getClass());
            System.out.println("" + scheduledTask.getTask().getClass());
        }
    }

}
