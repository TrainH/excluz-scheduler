package com.example.excluzscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExcluzSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExcluzSchedulerApplication.class, args);
    }
}
