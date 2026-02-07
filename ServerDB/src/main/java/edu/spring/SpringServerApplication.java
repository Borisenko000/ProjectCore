package edu.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class SpringServerApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringServerApplication.class, args);
    }
}
