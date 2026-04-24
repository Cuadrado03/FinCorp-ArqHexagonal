package com.fincorp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FinCorpApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinCorpApplication.class, args);
    }
}